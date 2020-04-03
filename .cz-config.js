'use strict';

module.exports = {

  types: [
    {
      value: 'WIP',
      name : '💪  WIP:      工作正在进行中'
    },
    {
      value: 'feat',
      name : '✨  feat:     添加了一个新特性'
    },
    {
      value: 'fix',
      name : '🐞  fix:      修复了一个幺蛾子'
    },
    {
      value: 'refactor',
      name : '🛠  refactor: 尝试重构了下代码'
    },
    {
      value: 'docs',
      name : '📚  docs:     修改了下说明文档'
    },
    {
      value: 'test',
      name : '🏁  test:     添加或修改测试代码'
    },
    {
      value: 'chore',
      name : '🗯  chore:    修改了代码无关的文件'
    },
    {
      value: 'style',
      name : '💅  style:    花里胡哨'
    },
    {
      value: 'revert',
      name : '⏪  revert:   败者食尘'
    }
  ],

  scopes: [],

  allowCustomScopes: true,
  allowBreakingChanges: ["feat", "fix"]
};
