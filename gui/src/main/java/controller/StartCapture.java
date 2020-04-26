package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;
import org.pcap4j.core.*;
import util.CNIF;
import util.DirHandle;
import util.PacketHandle;
import util.TableHandle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class StartCapture {
    private Manager manager;
    private String nifName;
    private String pcapPath;
    private boolean offline;
    private CaptureProperty captureProperty;
    private FilterProperty filterProperty;
    private CNIF cnif;

    @FXML
    Button filterAddButton;

    @FXML
    Button filterEditButton;

    @FXML
    Button filterDeleteButton;

    @FXML
    TextField expression;

    @FXML
    ComboBox<Label> direction;

    @FXML
    Button stopButton;

    @FXML
    Button applyButton;

    @FXML
    Button startButton;

    @FXML
    Button saveButton;

    @FXML
    TableView<Property> filterConfigTable;

    @FXML
    TableView<Property> statTable;

    // TODO: 2020/4/19 Packet info model
    @FXML
    TableView<Property> packetTable;

    public StartCapture() {
    }

    public void initialize() throws IOException {
        DirHandle.InitializeDirection(this.direction);
        try {
            TableHandle.InitializeTable(new FilterProperty(), filterConfigTable);
            TableHandle.UpdateConfigTable(SettingProperty.filterConfigFolder, new FilterProperty(), filterConfigTable);
        } catch (IOException e) {
            e.printStackTrace();
        }


        TableHandle.InitializeTable(new PacketProperty(), packetTable);

        stopButton.setDisable(true);

        File temp = new File(SettingProperty.tempPcapFolder + "/tmp.pcapng");
        temp.delete();

        // TODO: 2020/4/23 完成监听 
        filterConfigTable.selectionModelProperty().addListener(new ChangeListener<TableView.TableViewSelectionModel<Property>>() {
            @Override
            public void changed(ObservableValue<? extends TableView.TableViewSelectionModel<Property>> observableValue, TableView.TableViewSelectionModel<Property> propertyTableViewSelectionModel, TableView.TableViewSelectionModel<Property> t1) {
                FilterProperty filterProperty = (FilterProperty) t1.getSelectedItem();
                expression.setText(filterProperty.getExpression());
                Label label = new Label();
                switch (filterProperty.getDirection()) {
                    case INOUT:
                        label.setText("所有方向");
                        label.setId("allLabel");
                    case IN:
                        label.setText("入方向");
                        label.setId("inLabel");
                    case OUT:
                        label.setText("出方向");
                        label.setId("outLabel");
                }
                direction.setValue(label);
            }
        });

    }

    public void ReceiveFilterConfig(FilterProperty filterProperty) {
        if (this.filterProperty != filterProperty)
            this.filterConfigTable.getItems().add(filterProperty);
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setCaptureProperty(CaptureProperty captureProperty) {
        this.captureProperty = captureProperty;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
        startButton.setDisable(offline);
        if (offline) {
            this.cnif = new CNIF(this.pcapPath);
            FilterProperty filterProperty = new FilterProperty();
            filterProperty.setExpression("");
            filterProperty.setDirection(PcapHandle.PcapDirection.INOUT);
            OfflineTask task = new OfflineTask(this.cnif, filterProperty);
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    public void setNifName(String name) {
        this.nifName = name;
    }

    public void setPcapPath(String path) {
        this.pcapPath = path;
    }

    @FXML
    private void FilterAddButtonOnClicked() throws IOException {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/AddFilterConfig.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        AddFilterConfig addFilterConfig = loader.getController();
        addFilterConfig.setStartCapture(this);
        addFilterConfig.setFilterProperty(null);
        stage.show();
    }

    @FXML
    private void FilterEditButtonOnClicked() throws IOException {
        if (filterConfigTable.getSelectionModel().getSelectedItem() == null)
            return;
        this.filterProperty = (FilterProperty) filterConfigTable.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        URL url = loader.getClassLoader().getResource("view/AddFilterConfig.fxml");
        loader.setLocation(url);
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        AddFilterConfig addFilterConfig = loader.getController();
        addFilterConfig.setStartCapture(this);
        addFilterConfig.setFilterProperty(this.filterProperty);
        addFilterConfig.InitializeConfig();
        File file = new File(SettingProperty.filterConfigFolder + '/' + this.filterProperty.getName() + ".json");
        file.delete();
        stage.show();
    }

    @FXML
    private void FilterDeleteButtonOnClicked() {
        if (this.filterConfigTable.getSelectionModel().getSelectedItem() != null) {
            int index = this.filterConfigTable.getSelectionModel().getSelectedIndex();
            FilterProperty filterProperty = (FilterProperty) this.filterConfigTable.getItems().get(index);
            this.filterConfigTable.getItems().remove(index);
            File file = new File(SettingProperty.filterConfigFolder + '/' + filterProperty.getName() + ".json");
            if (file != null)
                file.delete();
            this.filterConfigTable.getSelectionModel().clearSelection();
        }
    }


    @FXML
    private void StopButtonOnClicked() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        this.cnif.handle.close();
        this.cnif.dumper.close();
    }

    @FXML
    private void ApplyButtonOnClicked() {
        packetTable.getItems().clear();
        if (this.filterProperty == null)
            this.filterProperty = new FilterProperty();
        this.filterProperty.setExpression(expression.getText());
        this.filterProperty.setDirection(DirHandle.GetDirection(this.direction));
        // TODO: 2020/4/22 一次性刷新，根据 this.filterProperty
        if (this.offline) {
            this.cnif = new CNIF(this.pcapPath);
            OfflineTask task = new OfflineTask(this.cnif, this.filterProperty);
            Thread thread = new Thread(task);
            thread.start();
        } else {
            File file = new File(SettingProperty.tempPcapFolder + "/tmp.pcapng");
            if (!file.exists())
                return;
            CNIF cnif = new CNIF(SettingProperty.tempPcapFolder + "/tmp.pcapng");
            OfflineTask task = new OfflineTask(cnif, this.filterProperty);
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    @FXML
    private void StartButtonOnClicked() {
        File temp = new File(SettingProperty.tempPcapFolder + "/tmp.pcapng");
        temp.delete();
        stopButton.setDisable(false);
        startButton.setDisable(true);
        this.ApplyButtonOnClicked();
        try {
            NIFProperty nifProperty = new NIFProperty(Pcaps.getDevByName(this.nifName));
            this.cnif = new CNIF(nifProperty, this.captureProperty);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }

        OnlineTask task = new OnlineTask(this.cnif, this.filterProperty);
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    private void SaveButtonOnClicked() {

    }


    // TODO: 2020/4/23 优化 task 结构
    private class OnlineTask extends Task<PacketProperty> {

        private CNIF cnif;
        private FilterProperty filterProperty;

        protected OnlineTask(CNIF cnif, FilterProperty filterProperty) {
            this.cnif = cnif;
            this.filterProperty = filterProperty;
        }

        @Override
        protected void updateValue(PacketProperty packetProperty) {
            super.updateValue(packetProperty);
            if (packetProperty != null)
                packetTable.getItems().add(packetProperty);
        }

        @Override
        protected PacketProperty call() throws Exception {
            CaptureProperty property;
            if (captureProperty != null)
                property = captureProperty;
            else {
                property = new CaptureProperty();
                property.setCount(-1);
                property.setTimeout(1000);
                property.setLength(65536);
                property.setBuffer(1024 * 1024 * 100);
            }
            boolean lop = false;
            int num = 0;
            if (property.getCount() < 0)
                lop = true;
            while (lop || num < property.getCount()) {
                try {
                    BpfProgram bpfProgram = this.cnif.handle.compileFilter(filterProperty.getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                    PcapPacket packet = this.cnif.handle.getNextPacketEx();
                    this.cnif.dumper.dump(packet);
                    if (bpfProgram.applyFilter(packet)) {
                        // 对符合过滤器的包进行处理
                        PacketProperty packetProperty = PacketHandle.Pipeline(packet);
                        packetProperty.setNo(num + 1);
                        this.updateValue(packetProperty);
                    }
                    num++;
                } catch (PcapNativeException e) {
                    System.out.println("PNative");
                    break;
                } catch (UnknownHostException e) {
                    System.out.println("UHost");
                    break;
                } catch (EOFException e) {
                    System.out.println("EOF");
                    break;
                } catch (TimeoutException ignored) {
                } catch (NotOpenException e) {
                    System.out.println("NOpen");
                    break;
                }
            }
            return null;
        }


    }

    private class OfflineTask implements Runnable {

        private CNIF cnif;
        private FilterProperty filterProperty;


        protected OfflineTask(CNIF cnif, FilterProperty filterProperty) {
            this.cnif = cnif;
            this.filterProperty = filterProperty;
        }

        @Override
        public void run() {
            int num=0;
            while (true) {
                try {
                    BpfProgram bpfProgram = this.cnif.handle.compileFilter(filterProperty.getExpression(), BpfProgram.BpfCompileMode.OPTIMIZE, (Inet4Address) InetAddress.getByName("255.255.255.255"));
                    PcapPacket packet = this.cnif.handle.getNextPacketEx();
                    if (bpfProgram.applyFilter(packet)) {
                        // 对符合过滤器的包进行处理
                        PacketProperty packetProperty = PacketHandle.Pipeline(packet);
                        packetProperty.setNo(num + 1);
                        packetTable.getItems().add(packetProperty);
                    }
                    num++;
                } catch (PcapNativeException e) {
                    System.out.println("PNative");
                    break;
                } catch (UnknownHostException e) {
                    System.out.println("UHost");
                    break;
                } catch (EOFException e) {
                    System.out.println("EOF");
                    break;
                } catch (TimeoutException ignored) {
                } catch (NotOpenException e) {
                    System.out.println("NOpen");
                    break;
                }
            }
        }
    }
}
