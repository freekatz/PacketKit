<p align="center">
  <a href="https://github.com/1uvu/pkit" target="_blank">
    <img src="res/logo.png" alt="Packet Kit Logo" width=72 height=72>
  </a>
  <h3 align="center">Packet Kit(PKit)</h3>
  <p align="center">
    一个强大的跨平台底层数据包工具箱
    <br>
    <a href="https://github.com/1uvu/pkit/issues/new?template=bug.md">Report bug</a>
    ·
    <a href="https://github.com/1uvu/pkit/issues/new?template=feature.md&labels=feature">Request feature</a>
  </p></p>


[![License](res/README/_license-mit-blue.svg)](https://opensource.org/licenses/MIT)




## 目录

- [基本信息](#基本信息)
- [物语](#物语)
- [安装](#安装)
- [使用](#使用)
- [文档](#文档)
- [贡献和报告](#贡献和报告)
- [致谢](#致谢)
- [版权许可](#版权许可)

## 基本信息

Packet Kit 是一个网络数据包工具箱，包括捕获、过滤、解析、存储、构造、编辑、发送、统计、分析及安全描述等功能。它基于 Java 平台开发，使用 JavaFx 作为图形开发库，使用 Pcap4J 作为数据包捕获与过滤库，进一步地说，Pcap4J 是基于 jna 实现调用 libpcap 和 npcap/winpcap 库的中间件。

目前阶段，Packet Kit 主要由三个模块构成：捕获、发送和分析。

[最新发行版本](https://github.com/1uvu/PacketKit/releases)

## 物语

本项目是我的**毕业设计**，代码量 1w+，最终凭借它，我的毕业设计取得了**优**。

**感兴趣的朋友可在本仓库基础上自行随意修改。**

## 安装

Packet Kit 已在以下平台进行过严格测试：

- Window 7+
- Linux（Ubuntu）
- MacOS

下载 jar 二进制文件，直接点击运行使用

注：需要 Java Jdk11+ 以及 libpcap 和 npcap/winpcap 环境支持，另外，由于捕获底层数据包的需要，Linux 和 Mac 还需要赋予管理员权限

## 使用

**捕获模式**：

捕获模块分为两种模式：离线和在线，可读取离线数据包，也可实时捕获。

基于缓存机制，过滤器可在读取或者捕获的过程中实时更新。

针对在线模式，还提供了捕获配置文件，可指定捕获数量、长度、延时及捕获模式等。

*离线模式*

![offline](res/README/offline.gif)

*在线模式*

![online](res/README/online.gif)

*离线过滤*

![filter-offline](res/README/filter-offline.gif)

*实时过滤*

![filter-online](res/README/filter-online.gif)

**发送模块**

发送模块也分为两种模式：发送和转发，可直接发送数据包，也可将数据包转发到其它的本地网卡。

发送的数据包既可以是自己构造的，也可以是导入的，既可以是原始包，也可以是修改之后的，既可以是单个包，也可以是包列表，还可以调整数据包列表的顺序。

还提供了发送配置文件，可指定发送数量、发送延迟及重试次数等。

*发送单个*

![send-one](res/README/send-one.gif)

*发送列表*

![send-all](res/README/send-all.gif)

*除此之外还有转发单个和转发列表功能*

**分析模块**

分析模块分为流量分析、通信状况、通联关系和安全描述四个方面，目前只支持对离线文件进行分析。

*分析功能概览*

![analysis](res/README/analysis.gif)

## 文档

**禁止任何形式的搬运、分享、二次修改，未经允许不得用于个人的课题答辩等用处，仅作交流之用，抄袭必究。**

- [项目介绍](doc/project-introduce.pptx)
- [项目结构](doc/project-structure.txt)
- [软件工程](doc/xml)
- [代码规范](doc/code-structure.md)
- [使用文档](wiki)
- [待办清单](doc/todo.md)
- [已知 Bug](doc/bug.md)

## 贡献和报告

**代码贡献**

有意贡献代码、完善项目，或者爱好 pcap、网络协议、java 开发的朋友，联系：mail:zjh98@vip.qq.com

**Bug 报告**

直接提 issue，如长时间不理，也可发邮件

## 致谢

[@TCPDump.ORG](https://tcpdump.org)

[@WinPcap.ORG](https://winpcap.org)

[@Namp.ORG](https://namp.org)

[@Wireshark.ORG](https://wireshark.org)

[@Pcap4J.ORG](https://pcap4j.org)

[@OpenJFX.IO](https://openjfx.io)

[@HighCharts](https://www.highcharts.com.cn/)

## 版权许可

[MIT License](LICENSE.md)

禁止任何盈利使用和二次修改，转发务必带着本仓库的 [Entry](https://github.com/1uvu/PacketKit)

Enjoy it :metal:
