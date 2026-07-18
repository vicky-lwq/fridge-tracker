# 冰箱食物管理App 界面设计方案

日期：2026年07月16日
适用范围：食物列表页、新增/编辑食物页、过期提醒通知文案与图标建议

---

## 一、Design Thinking 四问

**Purpose**：记录冰箱里的食物、提醒过期，面向日常做饭的普通用户。核心场景是放食材进冰箱时顺手记一笔、开冰箱门前扫一眼还有什么快过期，是高频轻量操作，不是深度数据分析工具。

**Tone**：自然有机（organic/natural），偏"厨房手记"的生活感，不做"数据仪表盘"式的科技感。用暖绿色调、柔和中性背景、口语化中文文案体现这个调性，全篇统一，不混搭其他风格。

**Constraints**：Android原生Jetpack Compose + Material3（项目CLAUDE.md强制要求）；minSdk26，意味着相当一部分设备在Android12以下、拿不到Dynamic Color，需要一套静态主题兜底；纯离线App，不用考虑网络加载态；单手操作优先，核心操作（FAB、保存按钮）放在拇指容易够到的位置。

**Differentiation**：过期日期显示成"7月20日（周一）"这种口语格式，不用"2026-07-20"这种冷冰冰的ISO写法；剩余天数用三色徽标+图标双通道让人一眼分辨轻重缓急；空状态和通知文案语气放松，不说教。这个App想让用户记住的是"记这件事不麻烦"，不是视觉炫技。

---

## 二、项目设计语言映射表（本方案唯一取值来源）

项目是全新空项目，没有可扫描的既有页面或token文件。以下取值按项目CLAUDE.md的强制要求（Material3 + Compose MaterialTheme）建立，作为本项目第一版设计系统，后续新页面从这里取值，不要另起一套。

### 2.1 色彩（Material3 ColorScheme，Light主题）

品牌色相收敛为一种（绿色家族，不同明度分层），另加2个语义色（琥珀、红），总计3个色相，符合"色相不超过3种"的克制要求。

| Token | 取值 | 用途 |
|---|---|---|
| `primary` | `#386A46` | 品牌主色，FAB、选中态强调、"正常"语义的隐性关联色 |
| `onPrimary` | `#FFFDF9` | primary容器上的文字/图标 |
| `primaryContainer` | `#B9F0B8` | 浅绿容器背景（如DatePicker选中日期） |
| `onPrimaryContainer` | `#062109` | primaryContainer上的文字 |
| `secondary` | `#52664F` | 中性橄榄，未选中chip、次要按钮 |
| `onSecondary` | `#FFFDF9` | — |
| `secondaryContainer` | `#D5EACE` | 筛选chip选中态背景 |
| `onSecondaryContainer` | `#101F0F` | 筛选chip选中态文字/图标 |
| `tertiary` | `#5B6E1A` | 青柠绿变体，与primary同色相不同明度，仅用于个别系统组件占位，UI里不主动大面积使用 |
| `onTertiary` | `#FFFDF9` | — |
| `tertiaryContainer` | `#DDEE9C` | — |
| `onTertiaryContainer` | `#181E00` | — |
| `error` | `#BA1A1A` | "已过期"语义色、删除类操作 |
| `onError` | `#FFFBFF` | — |
| `errorContainer` | `#FFDAD6` | "已过期"徽标背景、滑动删除的揭示背景 |
| `onErrorContainer` | `#410002` | "已过期"徽标文字/图标 |
| `background` / `surface` | `#FCFDF7` | 页面底色，暖白偏绿，非纯白 |
| `onBackground` / `onSurface` | `#1A1C18` | 主文字色，近黑非纯黑 |
| `surfaceVariant` | `#DFE4D7` | — |
| `onSurfaceVariant` | `#43483D` | 次级文字色（分类标签、辅助说明） |
| `outline` | `#73796C` | 输入框默认边框、未选中chip边框 |
| `outlineVariant` | `#C3C8BA` | — |
| `surfaceContainerLowest` | `#F7F9F1` | — |
| `surfaceContainerLow` | `#F1F4EB` | 列表卡片背景（默认态） |
| `surfaceContainer` | `#EBEEE5` | — |
| `surfaceContainerHigh` | `#E5E8DF` | 列表项内分类图标底、正常态天数徽标背景 |
| `surfaceContainerHighest` | `#E0E3D9` | — |

以下2个自定义语义色，M3默认角色没有覆盖"即将过期"这个状态，需要在`Color.kt`里额外定义并通过自定义`ExtendedColorScheme`（CompositionLocal）暴露，不要塞进M3标准角色里顶替：

| Token（自定义） | 取值 | 用途 |
|---|---|---|
| `warning` | `#8B5A00` | "即将过期"徽标文字/图标 |
| `onWarning` | `#FFFBFF` | — |
| `warningContainer` | `#FFDDA6` | "即将过期"徽标背景 |
| `onWarningContainer` | `#2B1800` | — |

**关于精确色值的提醒**：以上hex是设计方向的具体建议值，不是最终校验过对比度的成品。落地前建议用Material Theme Builder（m3.material.io）以`primary=#386A46`为种子色生成完整的官方tonal palette，核对每一组文字/背景组合的对比度，尤其是`warningContainer`/`onWarningContainer`这类自定义组合，M3官方工具不会自动帮你算，需要手动用对比度检测工具复核≥4.5:1。

**Dark主题**：本方案未展开夜间模式的具体色值。项目当前AC未提出夜间模式需求，列入待决策项。若后续需要，用同一seed色在Theme Builder里生成dark tonal palette即可，不需要另起设计语言。

**Dynamic Color**：Android12及以上可选启用系统Dynamic Color（跟随用户壁纸取色），但minSdk26要求必须有静态主题兜底，本文档给出的就是这套静态兜底方案，Dynamic Color作为锦上添花的可选项，不是本方案的必选项。

### 2.2 字体

项目内容以中文为主（食物名称、分类名、提示文案），这决定了字体选择的优先级：中文可读性优先于视觉个性，因为多数"有性格"的Google Fonts没有中文字形覆盖，装饰性中文字体在小字号下会牺牲辨识度，这条不能为了追求独特而妥协。

- **正文/UI字体**：`Noto Sans SC`（通过Compose Downloadable Fonts API引入），覆盖所有中文文案、按钮、标签、输入框。不用系统默认字体（各厂商Android ROM的默认无衬线字体表现不一致，小米/华为/三星风格都不同，统一指定字体是为了跨设备视觉一致，不是为了追求"独特"）。
- **点缀显示字体（可选）**：`Fraunces`（Google Fonts，暖调衬线，带手工感），只用在一处：列表项里剩余天数徽标的数字本身（如"3"），用`buildAnnotatedString`让数字用Fraunces、"天"字用Noto Sans SC，制造克制但有记忆点的细节。这是唯一的"个性字体"投入点，不扩散到其他地方。是否采纳见待决策项。
- 字重：正文400，标题/强调600，不在同一段文字里混用3种以上字重。

字号阶梯（Compose `Typography`，M3默认命名）：

| Token | sp | 用途 |
|---|---|---|
| `labelSmall` | 11sp | 分类标签、录入日期只读小字 |
| `labelMedium` | 12sp | 徽标辅助文字 |
| `labelLarge` | 14sp | 天数徽标主文字、按钮文字 |
| `bodyMedium` | 14sp | 表单supportingText、次级说明 |
| `bodyLarge` | 16sp | 表单输入内容、空状态正文 |
| `titleMedium` | 16sp | 食物名称 |
| `titleLarge` | 22sp | 页面标题 |

**注意**：所有文字用`sp`单位（跟随系统字体缩放设置），不要用`dp`，间距、圆角、图标尺寸才用`dp`。这个混淆在Compose里很常见，写进给code-writer的注意事项。

### 2.3 间距（8dp基准网格）

| Token | dp | 用途 |
|---|---|---|
| `space-1` | 4 | 徽标内图标与文字间距 |
| `space-2` | 8 | 列表卡片间垂直间距、筛选chip间距 |
| `space-3` | 12 | 卡片内元素间距 |
| `space-4` | 16 | 页面左右边距、表单按钮间距 |
| `space-6` | 24 | 表单字段之间的垂直间距 |
| `space-12` | 48 | 空状态图标与文字整体的上下留白 |

### 2.4 圆角（M3默认Shape scale）

| Token | dp | 用途 |
|---|---|---|
| `shape.extraSmall` | 4 | — |
| `shape.small` | 8 | **Chip**（筛选chip、正常提醒都是这个，不是胶囊形，M3标准chip就是8dp圆角矩形，不要做成全圆角） |
| `shape.medium` | 12 | 列表卡片、输入框 |
| `shape.large` | 16 | FAB（M3标准FAB是16dp圆角方形，不是正圆） |
| `shape.full` | 9999 | 天数徽标（唯一用到完全圆角的地方） |

### 2.5 阴影/层级

Material3优先用`surfaceContainer`色阶表达层级，不是靠阴影堆叠。列表卡片默认用`Elevation.Level1`（1dp阴影）+`surfaceContainerLow`背景，不需要更高阴影。

### 2.6 动效token

| Token | 时长 | 缓动 | 用途 |
|---|---|---|---|
| `duration-fast` | 150ms | 标准 | 输入框聚焦/错误态切换 |
| `duration-normal` | 200-250ms | 标准 | chip选中切换、菜单展开收起 |
| `duration-slow` | 300ms | FastOutSlowIn（M3 emphasized） | 页面转场 |

---

## 三、信息层级方案

### 食物列表页
- **第一层级**：食物名称（`titleMedium`，`onSurface`）、剩余天数徽标（`labelLarge`，按状态用`warning`/`error`/中性色）
- **第二层级**：分类筛选chip（`labelLarge`，选中态`onSecondaryContainer`）、分类文字标签（`labelSmall`，`onSurfaceVariant`）
- **第三层级**：录入日期等元信息（本页不展示，放在编辑页）

### 新增/编辑食物页
- **第一层级**：名称输入框、过期日期选择
- **第二层级**：分类选择、保存/取消按钮
- **第三层级**：录入日期只读小字

---

## 四、布局结构

### 食物列表页（Compact宽度，<600dp，手机竖屏）

```
┌─────────────────────────────┐
│ TopAppBar："冰箱食物"          │  titleLarge, surface底
├─────────────────────────────┤
│ [全部][蔬果][肉类][海鲜]→ 横向滚动 │  FilterChip行，48dp点击区
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │ 🥕 胡萝卜        剩3天    │ │  FoodItemCard，间距8dp
│ │    蔬菜水果              │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ 🥛 牛奶      已过期2天    │ │
│ │    蛋奶                 │ │
│ └─────────────────────────┘ │
│           ...                │
│                        ⊕FAB  │  56dp圆形，primary底色
└─────────────────────────────┘
```

- 单栏，全屏宽度，左右边距16dp
- 断点行为：本方案只覆盖Compact宽度类（手机竖屏，<600dp）。Medium/Expanded（平板/折叠屏）的双栏Master-Detail布局未设计，列入待决策项
- 空列表时，筛选chip行仍展示，列表区域替换为空状态

### 新增/编辑食物页（单栏居中表单）

```
┌─────────────────────────────┐
│ ← 新增食物                    │  TopAppBar，仅返回箭头
├─────────────────────────────┤
│  名称 *                      │
│  [___________________]      │  OutlinedTextField
│  录入于 2026年7月16日          │  labelSmall只读小字
│                              │
│  分类 *                      │
│  [蔬菜水果          ▼]      │  下拉选择，非chip
│                              │
│  过期日期 *                   │
│  [7月20日（周一）    📅]      │  只读，点击唤起DatePicker
│                              │
├─────────────────────────────┤
│  [取消]        [保存]         │  底部按钮组，各48dp高
└─────────────────────────────┘
```

- 表单宽度不设最大值限制（手机全宽即可，未来若适配平板可加`widthIn(max=480dp)`居中）
- 字段间距24dp，页面左右边距16dp

---

## 五、组件规格清单

### 5.1 FoodItemCard（食物列表项，新增组件）

- 容器：`Card`，`shape.medium`（12dp），`surfaceContainerLow`背景，`Elevation.Level1`（1dp），无边框
- 内容：`Row`，左侧40dp圆形容器装分类图标（8个分类各配一个outline风格图标，底色`surfaceContainerHigh`），中间`Column`（名称+分类文字标签），右侧天数徽标
- 名称：`titleMedium`，`onSurface`，单行，超长省略号截断（`maxLines=1, TextOverflow.Ellipsis`），不做多行换行，理由是保持列表项高度一致，避免卡片高度参差破坏纵向节奏
- 分类文字标签：`labelSmall`，`onSurfaceVariant`
- 天数徽标：`shape.full`胶囊，`labelLarge`文字，内容按状态区分：
  - 正常（>3天）：文案"剩X天"，背景`surfaceContainerHigh`，文字`onSurfaceVariant`，无图标（平静状态本身不需要额外提醒符号）
  - 即将过期（0-3天）：文案"剩X天"或"今天到期"（0天时用更友好的措辞代替"剩0天"），背景`warningContainer`，文字`onWarningContainer`，配14dp钟表outline图标
  - 已过期（负数）：文案"已过期X天"，背景`errorContainer`，文字`onErrorContainer`，配14dp警示三角outline图标
  - 颜色不是唯一信息载体：文字内容本身已经明确写出天数和状态词，图标是第二重强化，不是唯一判断依据

四态：
- 默认：如上
- press（点击进入编辑页前的反馈）：`onSurface` 12%透明度state layer叠加+ripple
- 无hover（移动端不适用）
- 无disabled（列表项始终可交互）

滑动删除交互（`SwipeToDismissBox`，M3官方API）：
- 拖动过程中背景揭示`errorContainer`色+`onErrorContainer`色垃圾桶图标，随拖动距离做alpha和scale渐入
- 未达阈值松手：spring动画弹回原位
- 超过阈值松手：200ms淡出+收缩后移除，紧接着弹出Snackbar"已删除'胡萝卜'"+"撤销"文字按钮，5秒后自动消失
- 撤销Snackbar是本方案默认加入的安全网，不是AC硬性要求，如果觉得增加了复杂度可以去掉，改成直接删除不可撤销，列入待决策项

### 5.2 FilterChip行（分类筛选，复用M3 `FilterChip`组件）

- 高度：视觉32dp，但Compose默认会把最小可交互区撑到48dp（前提是没有关闭`LocalMinimumInteractiveComponentEnforcement`），实现时确认这点没被误关
- 形状：`shape.small`（8dp圆角矩形，不是胶囊形）
- 默认（未选中）：背景`surface`，1dp边框`outline`，文字/图标`onSurfaceVariant`
- 选中：背景`secondaryContainer`，无边框，文字`onSecondaryContainer`，leading slot替换为check图标，切换动效200ms颜色渐变
- press：state layer叠加对应容器色的8-12%透明度+ripple
- 无disabled
- chip间距8dp，整行可横向滚动（`LazyRow`），这是刻意的横向滚动区域，不违反"移动端不横向滚动"这条（该条约束的是页面级横向滚动，筛选行的横向滚动是常见且合理的例外）

### 5.3 FAB（复用M3标准`FloatingActionButton`）

- 56dp圆形容器，`shape.large`（16dp圆角，M3标准FAB形状本就不是正圆，是16dp圆角方形）
- 默认：容器`primary`，图标（加号）`onPrimary`
- press：state layer叠加+elevation从Level3升至Level4（M3标准FAB press反馈）
- 无disabled（FAB始终可点）
- 位置：右下角，距边缘16dp

### 5.4 OutlinedTextField（名称输入框，复用M3标准组件）

- label：显示"名称"，必填字段在label文字后加"*"，同时给`Modifier.semantics { }`标注`isRequired`供无障碍工具读出（不能只靠视觉星号传达必填）
- placeholder：辅助示例"例如：胡萝卜"，不能替代label
- 失焦校验，不逐字符实时报错
- 四态：
  - 默认：1dp边框`outline`，label`onSurfaceVariant`
  - 聚焦：2dp边框`primary`，label上浮变`primary`色，150ms过渡
  - 错误：2dp边框`error`，label和supportingText变`error`色，supportingText紧贴字段下方显示"请输入食物名称"，trailing叹号图标（双通道，不靠颜色单独传达错误）
  - disabled：不适用于此字段（名称始终可编辑）

### 5.5 分类选择（`ExposedDropdownMenuBox`，新增组件，非chip）

与列表页筛选chip视觉不同，这是刻意的区分，不是不一致：筛选chip解决的是"一屏内快速切换多选视图"，下拉选择解决的是"表单里一次性选定单个值"，两种场景用不同组件形态是合理分工，不是同一功能的两套不一致实现。

- 关闭态：外观同`OutlinedTextField`（只读），trailing下拉箭头图标，点击展开菜单
- 展开态：`DropdownMenu`列出8个分类文字选项，当前选中项背景`secondaryContainer`高亮，每个菜单项高度48dp
- 收起动效：200ms淡出
- 必填校验同名称字段

### 5.6 过期日期选择（只读字段+M3标准`DatePickerDialog`）

- 字段本身只读，点击唤起系统标准`DatePicker`组件，不做自定义视觉
- 字段展示格式："7月20日（周一）"这类口语格式，不用ISO格式
- `confirmButton`="确定"，`dismissButton`="取消"
- 校验建议：过期日期不早于今天，如果这条规则要生效，需产品确认（见待决策项）

### 5.7 录入日期（只读小字，非表单控件）

- 位置：名称输入框正下方一行
- 样式：`labelSmall`，`onSurfaceVariant`，无边框无label装饰，纯文字"录入于2026年7月16日"，新增时固定为当天，编辑时保持原值不可改

### 5.8 保存/取消按钮

- 布局：底部`Row`，取消（`OutlinedButton`，权重1）+保存（`FilledButton`，权重2），间距16dp，各48dp高
- 保存按钮四态：
  - 默认：容器`primary`，文字`onPrimary`
  - press：state layer叠加+ripple
  - disabled（必填未填完时）：容器`onSurface` 12%透明度叠加在`surface`上，文字`onSurface` 38%透明度，不响应点击
- 取消按钮：`OutlinedButton`，边框`outline`，文字`onSurface`，始终可点，点击若有未保存修改弹确认对话框"放弃更改？"（继续编辑/放弃两个选项），没有修改则直接返回

### 5.9 空状态（列表为空时，新增组件）

- 居中布局，图标（96dp，outline风格冰箱或叶子图形，`onSurfaceVariant`色，避免复杂插画风格）+文字，图标与文字间距24dp
- 主文案（`bodyLarge`，`onSurface`）："冰箱空空，还没有食物"
- 次文案（`bodyMedium`，`onSurfaceVariant`）："点击右下角加号开始记录"
- 文字容器限制宽度（约240dp）居中，控制断行位置，不依赖整屏宽度自动换行

---

## 六、交互与动效规范

| 交互 | 时长 | 缓动 | 说明 |
|---|---|---|---|
| 输入框聚焦/错误态切换 | 150ms | 标准 | M3默认TextField动效 |
| chip选中切换 | 200ms | 标准 | 背景色+图标淡入 |
| 下拉菜单展开/收起 | 200ms | 标准 | — |
| 滑动删除揭示背景 | 跟随手势 | 线性映射 | 松手后按阈值判断走spring回弹或200ms淡出收缩 |
| 页面转场（列表→新增页） | 300ms | FastOutSlowIn | Navigation默认转场 |
| 弹窗（DatePicker/确认对话框） | 入场稍慢出场稍快 | M3标准 | 遵循入场略慢出场略快的通用原则 |

Loading态：本App纯本地Room数据库读写，正常情况下毫秒级完成，不需要全屏loading spinner。保存按钮点击后立即禁用防止重复提交，文字可短暂变为"保存中…"，不额外弹出遮罩。

需要尊重系统"减少动画"辅助功能开关：本方案全部使用Compose标准动画API（`animate*AsState`、`AnimatedVisibility`等），这些API默认会响应系统`prefers-reduced-motion`等价设置，未引入需要手动适配的自定义粒子/手势特效，风险低，仍建议实现后手动开启系统减少动画选项测一遍。

---

## 七、过期提醒通知（文案与图标建议，不涉及排版）

- **小图标**（状态栏，24dp）：单色透明背景轮廓图标，建议用简化的叶子或冰箱轮廓，符合Android通知小图标规范（必须是纯色轮廓+透明底，不能用彩色图标）
- **大图标**（可选）：App圆形品牌图标，`primary`绿色背景+白色图形
- **文案风格**：简洁汇总，不制造焦虑
  - 标题："冰箱里有X件食物需要注意"（X=即将过期+已过期总数）
  - 内容：≤3件时全部列出食物名，超过3件列前3个+"等X件"，例如"胡萝卜、牛奶、虾仁等5件即将过期或已过期"
  - 当天汇总数为0时不发送通知，避免无意义打扰
- **点击通知后跳转**：默认只打开App首页，不做隐式筛选跳转，这个范围收窄是刻意的，是否需要跳转到"仅显示需注意食物"的过滤视图列入待决策项

---

## 八、设计依据摘要

- 品牌色相收敛为绿色家族一种+2个语义色：对应禁止事项"色相不超过3种，收敛到1主色加1-2语义色"
- 天数徽标用文字+图标双通道：对应工程正确性底线"颜色不作唯一信息载体"
- 中文正文用Noto Sans SC而非追求个性字体：CJK小字号可读性优先于frontend-design的"避免泛滥字体"原则，两者冲突时按工程可读性让步，冲突点已在下方待决策项说明
- 列表+筛选+FAB的标准清单式布局：这是工具型App用户最熟悉的操作路径，frontend-design"避免可预测布局套路"更适用于品牌/营销类页面，对效率工具，可预测性本身就是设计目标，不是缺陷
- 筛选chip和分类下拉外观不同：对应"两个长得不一样但功能相同的组件"自检项，已确认是场景差异（多选浏览vs单选录入）导致的合理分工，不是不一致
- 过期日期用口语格式："让记录这件事不麻烦"这一差异化目标的具体落地
- FAB形状16dp圆角、chip形状8dp圆角非胶囊：对应M3官方Shape规范，常被误做成圆形/胶囊形，特别标注防止实现出错

---

## 九、待决策项

1. 过期日期是否要限制"不能早于今天"，需要产品确认这条校验规则是否生效
2. 是否需要适配平板/折叠屏（Medium/Expanded宽度类）的双栏布局，本方案只覆盖手机Compact宽度
3. 点击每日汇总通知后，是否要跳转到"仅显示即将过期+已过期"的过滤视图，还是直接打开首页（当前方案默认后者，更简单）
4. 滑动删除是否需要"撤销"Snackbar，当前方案默认加入作为安全网，非AC硬性要求，可视实现成本取舍
5. 天数徽标数字用Fraunces字体点缀是否采纳，这是唯一的个性化字体投入点，不采纳则全部统一用Noto Sans SC，实现成本更低
6. 是否需要Dark主题，当前只给出Light主题完整色值

---

## 十、给code-writer的交付说明

**扫描来源**：项目根目录`CLAUDE.md`（技术栈、数据schema、目录结构、过期规则）；`~/.claude/skills/frontend-design/SKILL.md`；`~/.claude/rules/ui_engineering_baseline.md`。项目为全新空项目，无既有代码或设计token可扫描，本文档即为项目第一版设计系统。

**新增组件**（按项目目录约定放入`ui/components/`）：
- `FoodItemCard.kt`：列表项卡片
- `ExpiryBadge.kt`：天数状态徽标（内含正常/即将过期/已过期三态渲染逻辑）
- `CategoryFilterChipRow.kt`：列表页筛选chip行
- `CategoryDropdownField.kt`：新增/编辑页分类下拉
- `EmptyState.kt`：空状态

**采纳的frontend-design审美方向**：
- 字体：中文用Noto Sans SC保证可读性，仅在天数徽标数字点缀Fraunces制造记忆点（可选，见待决策项）
- 配色：暖调森林绿为主色，不用紫色渐变或泛滥配色套路
- 运动：动效只用在页面转场、滑动删除、状态切换这几个高冲击时刻，不做满屏微交互
- 空间：列表页留白克制，表单字段间距24dp保证呼吸感，不堆砌
- 背景：不用纹理/渐变，靠`surfaceContainer`色阶层级和留白建立空间感，符合"够用不繁琐"的项目定位，不需要额外装饰

**反AI套路自检结果**：字体未用Inter/Roboto/Arial泛滥选择（中文用Noto Sans SC是CJK可读性下的务实选择，非个性字体，已在设计依据里说明这个取舍）；配色非紫色渐变白底套路；列表+FAB+筛选chip布局属于工具型App的标准范式，本方案有意识地选择遵循这个可预测模式而非为了独特牺牲效率，已在设计依据中说明理由；context-specific character体现在状态徽标三色系统、口语化到期日期格式、克制的文案语气这几处具体细节，不是泛用模板。

**实施注意事项**：
- 文字全部用`sp`单位，间距/圆角/图标尺寸用`dp`，不要混淆
- Chip的形状是`shape.small`（8dp圆角矩形），FAB是`shape.large`（16dp圆角方形），两个都不是常见误做的胶囊形/正圆
- Chip视觉高度32dp但需确认Compose默认48dp最小可交互区没有被意外关闭
- DatePicker用Compose Material3标准组件，不要自定义视觉
- 滑动删除用`SwipeToDismissBox`官方API实现
- 本文档给出的色值hex是方向性建议，落地前用Material Theme Builder以`#386A46`为种子色生成官方tonal palette并核对对比度，尤其是自定义的`warning`/`warningContainer`组合
- `warning`相关token不属于M3标准`ColorScheme`，需要额外定义`ExtendedColorScheme`并通过`CompositionLocal`提供，不要塞进`tertiary`或其他标准角色里顶替
- Dynamic Color仅在API31+可选启用，minSdk26要求必须有本文档定义的静态主题兜底
