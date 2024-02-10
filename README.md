<h1 align="center">HMCL-PE非官方版</h1>

<h2>注：该项目即将停止更新，有能力的可自行克隆项目修复BUG</h2>
<h2>注：该项目即将停止更新，有能力的可自行克隆项目修复BUG</h2>
<h2>注：该项目即将停止更新，有能力的可自行克隆项目修复BUG</h2>
<details>
<summary>相较于官方版修复的BUG & 新增功能</summary>

- 适合服务器客户端使用，内置了直装模块 [assets/.minecraft]
- 去除res资源目录混淆以便用户修改
- 支持删除原整合包数据后在释放文件
- 支持释放到公有目录或私有目录
- 支持使用文本编写外置皮肤站地址 [assets/authlib_injector_server.json]
- 可启动1.19.3+ [Boat、PojavLauncher后端均可]
- 修复1.13.X-1.15.X版本中游戏内材质大面积透视BUG [仅PojavLauncher后端修复]
- 支持自定义布局文件 [assets/control/Default]
- 提供源码让用户自行改动项目，有文本说明提示部分类文件作用
- 支持在配置文件中自定义某些设置 [assets/config.properties]
- 修复使用“皮肤站”方式登录时，如果皮肤站链接非https协议部分用户无法登录问题
- 修复使用“皮肤站”方式登录时，使用Java8启动游戏崩溃BUG
- 修复使用“皮肤站”方式登录时，如果账户内有多个角色时选择任意一个进入服务器提示“无效的会话...”
- 修复使用“皮肤站”方式登录时，有时候角色没有皮肤BUG
</details>

<details>
<summary>目前还存在的问题</summary>

- 需要更新PojavLauncher后端才能使用Angle渲染器
- 需要修复按键的"视角跟随"功能引发的乱飞BUG
- 需要增加Java11、Java21运行环境
- 目前无法启动1.20Forge端，原因：似乎是modlauncher加载存在BUG？
</details>
