'use strict';

module.exports = {

  types: [
    {
      value: '💪  WIP(正在工作)',
      name : 'WIP:      工作正在进行中'
    },
    {
      value: '✨  feat(新的特性)',
      name : 'feat:     添加了一个新特性'
    },
    {
      value: '🐞  fix(大幺蛾子)',
      name : 'fix:      修复了一个幺蛾子'
    },
    {
      value: '🛠  refactor(重构代码)',
      name : 'refactor: 尝试重构了下代码'
    },
    {
      value: '📚  docs(说明文档)',
      name : 'docs:     修改了下说明文档'
    },
    {
      value: '🏁  test(测试代码)',
      name : 'test:     添加或修改测试代码'
    },
    {
      value: '🗯  chore(例行公事)',
      name : 'chore:    修改了代码无关的文件'
    },
    {
      value: '💅  style(花里胡哨)',
      name : 'style:    花里胡哨'
    },
    {
      value: '⏪  revert(败者食尘)',
      name : 'revert:   败者食尘'
    }
  ],

  scopes: [],

  allowCustomScopes: true,
  allowBreakingChanges: ["feat", "fix"]
};