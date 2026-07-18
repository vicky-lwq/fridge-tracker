# 冰箱食物管理 App 项目规范

## 项目定位

安卓原生App，记录冰箱里的食物库存，支持分类和过期提醒。目标是"够用不繁琐"：核心是录入食物名称+日期，分类和提醒是围绕这个核心的最小必要功能，不做库存统计、菜谱推荐、多冰箱管理等扩展功能，除非用户明确要求。

## 技术栈

- 语言：Kotlin
- UI：Jetpack Compose + Material 3
- 架构：MVVM（Compose UI → ViewModel → Repository → Room DAO）
- 本地数据库：Room
- 过期提醒：WorkManager（每日定时检查）+ NotificationCompat 本地通知
- minSdk 26，targetSdk 最新稳定版
- 包名：`com.fridgetracker.app`
- 无网络依赖，纯离线App，不接第三方SDK

## 目录结构约定

```
app/src/main/java/com/fridgetracker/app/
├── data/
│   ├── FoodItem.kt          # Room Entity
│   ├── FoodCategory.kt      # 分类枚举
│   ├── FoodDao.kt
│   └── FridgeDatabase.kt
├── repository/
│   └── FoodRepository.kt
├── notification/
│   └── ExpiryCheckWorker.kt # WorkManager定时检查过期
├── ui/
│   ├── theme/               # Color.kt / Type.kt / Theme.kt
│   ├── list/                # 食物列表页
│   ├── addedit/             # 新增/编辑食物页
│   └── components/          # 可复用组件（食物卡片、分类标签等）
└── viewmodel/
    └── FoodViewModel.kt
```

## 数据字段schema（FoodItem）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键自增 |
| name | String | 食物名称，必填 |
| category | FoodCategory | 分类枚举，必填 |
| addedDate | LocalDate | 录入日期，默认今天 |
| expiryDate | LocalDate | 过期日期，必填 |

分类枚举固定集合（不做用户自定义分类，保持简单）：蔬菜水果、肉类、海鲜、蛋奶、速冻食品、饮品、调味品、其他。

## 过期提醒规则

- 剩余天数 ≤ 3天（含已过期）视为"即将过期"，列表里高亮显示
- 每日固定时间（如上午9点）通过WorkManager检查一次，对即将过期和已过期的食物合并发一条本地通知
- 不做单独食物的定时提醒（避免通知过多），只做每日汇总

## 设计系统

- 遵循Material 3 Design规范，用Compose的`MaterialTheme`承载色彩、字体、形状token，不手写魔法数字
- 色彩：主题色跟"新鲜食物友好感"相关（如自然绿），语义色区分正常/即将过期/已过期三种状态
- 圆角、间距遵循Material 3默认比例（8dp基准网格）
- 空状态、加载态、列表项的hover/press反馈都要有

## 开发环境说明

本机无Android Studio，无法本地编译验证。代码交付后需要用户自行在Android Studio中打开`app`目录、Gradle Sync、连接模拟器或真机运行验证。
