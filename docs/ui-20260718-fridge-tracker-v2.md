# 冰了个箱 界面设计方案 v2

日期：2026年07月18日
适用范围：食物列表页（新增分类抽屉、统计栏、二次确认删除）、新增/编辑食物页（分类改选填、过期日期双模式输入、新增重量数量字段）、App改名视觉、整体配色改版
基础版本：`docs/ui-20260716-fridge-tracker.md`（v1）。本文档是v1的迭代，不推翻v1已定的字体、圆角、间距、动效体系，只对v1做增量修改。每个改动点都标注相对v1的差异。

---

## 一、Design Thinking 四问（v2更新）

**Purpose**：不变。记录冰箱里的食物、提醒过期，面向日常做饭的普通用户，高频轻量操作。

**Tone**：从v1的"自然有机/厨房手记"收紧为"清新时尚"，二者不是替换关系，是同一自然主题下更轻快的执行方式：颜色从偏暗偏黄的森林绿转向更亮更冷调的鲜绿，背景从暖米白转向冷调薄荷白。App改名"冰了个箱"（"X了个X"式网络流行语构词）本身也在强化这个更轻松诙谐的调性，跟色彩改版方向一致，不是两件孤立的事。

**Constraints**：不变，见v1。新增约束：本次改动基于已实现的v1代码迭代，颜色/字体/圆角/间距/动效token除非本文档明确写"变更"，否则一律沿用v1现有取值，不重新定义命名方式。

**Differentiation**：延续v1的口语化到期日期格式和三色徽标双通道。新增差异点：分类导航收进抽屉侧滑并配实景照片，让"翻冰箱找分类"这件事更像翻一本图文并茂的手账，而不是划一排文字标签；App名字本身的诙谐感延伸到视觉基调上。

---

## 二、项目设计语言映射表（v1基础 + v2变更，本方案唯一取值来源）

### 2.1 色彩 —— 本次改版核心变更区

v1色值偏暖偏稳重（森林绿+暖米白背景），v2整体转向冷调、更高饱和度的鲜绿，营造"清新时尚"感。**色相家族不变**（绿色主品牌 + 琥珀警示 + 红色错误，仍是3色相封顶），只调整同一色相内的明度/饱和度倾向，以及背景基调，符合"色相不超过3种"的克制要求，也符合用户"不需要推翻重来"的边界。

以下是`Color.kt`里各token的v1→v2取值对照，token命名不变，只改数值：

| Token（沿用v1命名，不改名） | v1值（暖调森林绿） | v2值（冷调鲜绿） | 变更说明 |
|---|---|---|---|
| `md_theme_light_primary` | `#386A46` | `#159A66` | 提亮、提高饱和度，从偏橄榄的暗绿转向更鲜亮的翡翠绿 |
| `md_theme_light_onPrimary` | `#FFFDF9`（暖白） | `#FBFFFC`（冷白） | 跟随背景基调从暖白转冷白 |
| `md_theme_light_primaryContainer` | `#B9F0B8` | `#9EF2C4` | 容器色跟随primary色相微调，略偏薄荷 |
| `md_theme_light_onPrimaryContainer` | `#062109` | `#00210F` | — |
| `md_theme_light_secondary` | `#52664F`（偏橄榄） | `#4C7A63`（偏鲜薄荷） | 去掉v1的橄榄黄调，转向更纯净的青绿，是"清新"感的主要来源之一 |
| `md_theme_light_onSecondary` | `#FFFDF9` | `#FBFFFC` | — |
| `md_theme_light_secondaryContainer` | `#D5EACE` | `#CBF0DC` | — |
| `md_theme_light_onSecondaryContainer` | `#101F0F` | `#082017` | — |
| `md_theme_light_tertiary` | `#5B6E1A` | `#3B6E39` | 未直接用于自定义组件，只是M3系统占位色，同步微调避免和新主色系脱节 |
| `md_theme_light_onTertiary` | `#FFFDF9` | `#FBFFFC` | — |
| `md_theme_light_tertiaryContainer` | `#DDEE9C` | `#C3EFC0` | — |
| `md_theme_light_onTertiaryContainer` | `#181E00` | `#04210A` | — |
| `md_theme_light_error` / `onError` / `errorContainer` / `onErrorContainer` | `#BA1A1A`系 | **不变** | 语义色，本次改版不涉及，保持v1取值 |
| `md_theme_light_background` / `surface` | `#FCFDF7`（暖米白） | `#F5FBF8`（冷薄荷白） | 背景基调转轻快是本次改版第二个主要来源 |
| `md_theme_light_onBackground` / `onSurface` | `#1A1C18`（暖近黑） | `#151E19`（冷近黑） | 跟随背景色温调整 |
| `md_theme_light_surfaceVariant` | `#DFE4D7` | `#DCE8E0` | — |
| `md_theme_light_onSurfaceVariant` | `#43483D` | `#3F4A44` | — |
| `md_theme_light_outline` | `#73796C` | `#6F7A73` | — |
| `md_theme_light_outlineVariant` | `#C3C8BA` | `#BFCBC3` | — |
| `md_theme_light_surfaceContainerLowest` | `#F7F9F1` | `#EFFAF4` | — |
| `md_theme_light_surfaceContainerLow` | `#F1F4EB` | `#E9F6EF` | 列表卡片背景，色阶逻辑不变，仅色温转冷 |
| `md_theme_light_surfaceContainer` | `#EBEEE5` | `#E3F1E8` | — |
| `md_theme_light_surfaceContainerHigh` | `#E5E8DF` | `#DDEBE1` | 列表项分类图标底色、正常态天数徽标背景 |
| `md_theme_light_surfaceContainerHighest` | `#E0E3D9` | `#D7E5DA` | — |
| `md_theme_light_warning`系（4个扩展token） | `#8B5A00`系 | **不变** | 语义色，不在本次改版范围 |

Dark主题：v1已在代码里落地了一套完整dark scheme（v1文档本身未展开，是code-writer实现时补的）。v2同步给出方向性推算值，规则同v1保持一致的明暗映射关系，**同样未经Material Theme Builder核验对比度**，这个提醒v1就提过，v2的新值一样需要走一遍官方工具复核，尤其是新的`surfaceContainer`系列：

| Token | v2 dark方向值 |
|---|---|
| `md_theme_dark_primary` | `#7FDBA8` |
| `md_theme_dark_onPrimary` | `#003920` |
| `md_theme_dark_primaryContainer` | `#00522E` |
| `md_theme_dark_onPrimaryContainer` | `#9EF2C4` |
| `md_theme_dark_secondary` | `#B0D4C0` |
| `md_theme_dark_onSecondary` | `#1B3A2A` |
| `md_theme_dark_secondaryContainer` | `#334F41` |
| `md_theme_dark_onSecondaryContainer` | `#CBF0DC` |
| `md_theme_dark_background` / `surface` | `#0E1512` |
| `md_theme_dark_onBackground` / `onSurface` | `#DDE4DF` |
| `md_theme_dark_surfaceVariant` | `#3F4A44` |
| `md_theme_dark_onSurfaceVariant` | `#BFCBC3` |
| `md_theme_dark_outline` | `#899690` |
| `md_theme_dark_outlineVariant` | `#3F4A44` |
| `md_theme_dark_surfaceContainerLowest` | `#090F0C` |
| `md_theme_dark_surfaceContainerLow` | `#151E19` |
| `md_theme_dark_surfaceContainer` | `#19221C` |
| `md_theme_dark_surfaceContainerHigh` | `#232D26` |
| `md_theme_dark_surfaceContainerHighest` | `#2E3830` |
| `md_theme_dark_error`/`warning`系 | **不变**，沿用v1现有dark值 |

`ExtendedColorScheme`（`ExtendedColors.kt`）结构不变，只是内部引用的`md_theme_light_warning`等token数值不变，本次不用改这个文件。

### 2.2 字体、间距、圆角、阴影、动效 —— 沿用v1，不做改动

- 字体：`NotoSansSC`本地字体不变，字号阶梯（`labelSmall`11sp到`titleLarge`22sp）不变，字重规则（正文400/强调600）不变。用户本次要求只针对"绿色明度饱和度"和"背景基调"，未涉及字体，不擅自扩大改动范围。
- 间距：8dp基准网格，`space-1`到`space-12`token不变。
- 圆角：`shape.extraSmall`4dp / `small`8dp / `medium`12dp / `large`16dp / `full`（徽标专用）不变。v2新增的抽屉分类图片沿用`shape.small`（8dp圆角矩形），不新增圆角阶梯。
- 阴影：列表卡片`Elevation.Level1`+`surfaceContainerLow`背景不变。
- 动效：`duration-fast`150ms / `duration-normal`200-250ms / `duration-slow`300ms不变，v2新增的抽屉展开、过期输入模式切换都复用这套已有token，不新增时长档位。

---

## 三、信息层级方案（v2更新）

### 食物列表页

- **第一层级**：食物名称（`titleMedium`，`onSurface`）、剩余天数徽标（`labelLarge`，按状态取色，沿用v1）
- **第二层级（新增）**：统计栏三个数字（`titleLarge`，按语义取色：总数`onSurface`中性、即将过期`extendedColors.warning`、已过期`colorScheme.error`），分类文字标签（`labelSmall`，`onSurfaceVariant`，有分类才显示）
- **第三层级**：录入日期等元信息（本页不展示）

统计栏放在第二层级而不是第一层级：它是"看一眼心里有数"的辅助信息，食物列表本身（用户要具体处理的对象）仍是本页最重要的内容，统计栏字号（`titleLarge`22sp）明显小于如果做成"大屏数字"该有的尺寸，克制处理避免抢夺列表的主体地位。

### 分类抽屉（新增页面区域）

- **第一层级**：分类图片+名称（一组，图文等重）
- **第二层级**：无

### 新增/编辑食物页

- **第一层级**：名称输入框、过期日期选择（不变）
- **第二层级**：分类选择（降级为选填，视觉权重不变，仍是`labelLarge`标签+独立字段，只是去掉必填标记）、重量/数量字段（新增，选填）、保存/取消按钮
- **第三层级**：录入日期只读小字

---

## 四、布局结构（v2更新）

### 4.1 食物列表页

```
┌─────────────────────────────────────┐
│ ☰  冰了个箱                          │  TopAppBar：新增leading菜单图标，点击展开抽屉；标题由"冰箱食物"改为"冰了个箱"
├─────────────────────────────────────┤
│ ┌───────────────────────────────┐   │
│ │    12        3         1       │   │  统计栏：Card容器，surfaceContainerLow背景，shape.medium(12dp)，
│ │  食材总数   即将过期    已过期    │   │  左右边距16dp，内边距16dp，三列等宽Row，数字titleLarge/标签labelSmall
│ └───────────────────────────────┘   │
├─────────────────────────────────────┤
│ ┌─────────────────────────────┐     │
│ │ 🥕 胡萝卜        剩3天        │     │  FoodItemCard，结构不变（见5.1）
│ │    蔬菜水果                   │     │
│ └─────────────────────────────┘     │
│ ┌─────────────────────────────┐     │
│ │ 🥛 牛奶      已过期2天        │     │  ← 无分类的条目，第二行分类文字整行不渲染，卡片自然更矮
│ └─────────────────────────────┘     │
│              ...                     │
│                            ⊕FAB      │
└─────────────────────────────────────┘
```

抽屉（`ModalNavigationDrawer`，默认收起，点击☰从左侧滑入，M3标准转场时长，不新增自定义时长）：

```
┌───────────────────────┐
│ 分类                    │  titleMedium，onSurface，内边距space-4(16dp)
├───────────────────────┤
│ [▦48x48] 全部           │  ← 无照片，用outline图标(如Icons.Outlined.GridView)
│ [🖼48x48] 蔬菜水果       │  ← 实景照片，shape.small(8dp)圆角矩形
│ [🖼48x48] 肉类           │
│ [🖼48x48] 海鲜           │
│ [🖼48x48] 蛋奶           │
│ [🖼48x48] 速冻食品       │
│ [🖼48x48] 饮品           │
│ [🖼48x48] 调味品         │
│ [🖼48x48] 其他           │
└───────────────────────┘
```

- 移除v1的`CategoryFilterChipRow`横向chip行，列表页顶部不再有筛选chip，筛选功能完全转移到抽屉
- 抽屉宽度用M3 `ModalDrawerSheet`默认宽度，不额外指定
- 空列表时统计栏依然展示（三个数字可能是`0/0/0`），列表区域替换为v1既有的`EmptyState`
- 统计栏取值范围：反映**全部食物**（不随抽屉筛选联动），理由见"设计依据"

### 4.2 新增/编辑食物页

```
┌─────────────────────────────────────┐
│ ← 新增食物                            │  不变
├─────────────────────────────────────┤
│  名称 *                              │  不变
│  [___________________]              │
│  录入于2026年7月18日                  │  不变
│                                      │
│  分类                                │  ← 去掉"*"，label改为"分类"
│  [选填，可不选           ▼]          │  ← placeholder文案变化，见5.4
│                                      │
│  过期日期 *                          │  ← 仍必填，不变
│  ┌───────────┬───────────┐          │
│  │  选日期    │  选天数    │          │  SegmentedButton，新增
│  └───────────┴───────────┘          │
│  [7月20日（周一）        📅]          │  选日期模式：不变的只读字段+DatePicker
│  ── 或（切换后）──                    │
│  [   3   ]  天后到期                 │  选天数模式：数字输入+固定文案
│                                      │
│  重量/数量                           │  ← 新增字段，无"*"
│  [  1.5  ]     [斤        ▼]        │  数字输入(weight 2) + 单位下拉(weight 1)
│                                      │
├─────────────────────────────────────┤
│  [取消]              [保存]          │  不变
└─────────────────────────────────────┘
```

- 字段整体顺序：名称→录入日期→分类→过期日期→重量/数量→按钮，只在过期日期后插入新字段，其余字段位置不挪动
- 字段间距沿用v1的`space-6`(24dp)

---

## 五、组件规格清单

### 5.1 FoodItemCard（沿用v1结构，本次只改两处）

**不变部分**：40dp圆形分类图标容器（`surfaceContainerHigh`底色）、`titleMedium`名称单行省略号截断、`ExpiryBadge`天数徽标、`shape.medium`卡片圆角、`Elevation.Level1`。

**变更1 —— 分类文字标签改为条件渲染**：
- `item.category`非空时：渲染`labelSmall`/`onSurfaceVariant`分类文字，位置不变（名称下方一行）
- `item.category`为空时：这一行`Text`整体不渲染（不是渲染空字符串占位），`Column`高度自然收缩，卡片高度比有分类的条目矮一些
- 这会导致列表里不同卡片高度不完全一致（有分类的两行、无分类的一行），是可接受的设计取舍，类比通讯录"副标题字段缺失时不留空行"的常见模式，不算视觉缺陷

**变更2 —— 滑动删除改为二次确认，去掉Snackbar撤销**：
- 拖动手势和背景揭示视觉（`errorContainer`背景+垃圾桶图标）保持v1不变
- 行为变化：滑动越过阈值松手后，**卡片先弹回原位**（不是像v1那样直接播放200ms收缩消失动画），随即弹出`AlertDialog`二次确认：
  - title：`删除「{截断后的食物名}」？`（食物名超过12个字符时截断+省略号，避免超长自由文本撑爆弹窗标题换行）
  - text：`删除后不可恢复。`（因为撤销Snackbar被移除，这里补一句提醒，弥补"删除即不可逆"的信息缺口）
  - confirmButton：`TextButton`，文字"删除"，颜色`colorScheme.error`（destructive操作着色，文字本身"删除"二字已经是主要信息载体，颜色是双重强化不是唯一载体）
  - dismissButton：`TextButton`，文字"取消"，默认色
- 点"取消"：对话框关闭，卡片此时已经弹回原位（不需要额外处理，因为松手那一刻已经弹回）
- 点"删除"：对话框关闭，播放v1既有的200ms `shrinkVertically`+`fadeOut`收缩动画，动画结束后真正调用删除
- 这个顺序（先弹回再弹窗，而不是保持卡片揭开状态等对话框结果）是M3滑动删除+二次确认的标准实现方式，避免依赖`SwipeToDismissBoxState`的挂起态`reset()`和对话框显示时机产生竞态，工程上更稳妥
- 四态：默认/press同v1不变；无hover；无disabled

### 5.2 CategoryDrawerContent（新增组件，替代`CategoryFilterChipRow`在列表页的使用）

- 容器：`ModalNavigationDrawer` + `ModalDrawerSheet`，背景`surfaceContainerLow`（跟列表卡片同一色阶语言）
- 顶部标题："分类"，`titleMedium`，`onSurface`，内边距`space-4`（16dp）四周
- 列表项（9行：全部+8个分类）：
  - 每行高度56dp，横向内边距`space-4`（16dp），图片/图标与文字间距`space-3`（12dp）
  - "全部"行：48x48dp圆角矩形（`shape.small`），底色`surfaceContainerHigh`，居中一个outline风格图标（如网格图标），无实景照片
  - 8个分类行：48x48dp圆角矩形（`shape.small`），内容是该分类的写实代表照片（1:1裁切，素材来源和获取方式不属于本方案范围，由项目后续自行提供，`shape.small`圆角矩形而非圆形是刻意选择，因为食物写实照片按圆形裁切容易切掉主体，矩形更适合承载照片内容）
  - 文字：`titleMedium`（跟列表页食物名称同字号，保持"这是一个可点选的一级导航项"的视觉分量），未选中`onSurface`，选中态`onSecondaryContainer`
  - 选中态：整行背景`secondaryContainer`，`shape.small`圆角裁切整行背景（不是只圆角图片），跟FilterChip沿用的选中配色token一致，视觉语言连续
  - press态：`onSurface` 8-12%透明度state layer叠加+ripple，跟卡片、chip的press反馈规则一致
  - 无disabled（所有分类始终可选）
- 交互：点击任意行后，抽屉以M3默认动效收起（约250ms），列表按选中分类过滤（无分类的食物只在"全部"下可见，这个过滤逻辑v1的`FoodViewModel.filteredFoods`已经天然支持，不需要额外改动）
- 当前选中项在抽屉重新打开时保持高亮，不重置

### 5.3 StatsBar（新增组件）

- 容器：`Card`，`surfaceContainerLow`背景，`shape.medium`（12dp），`Elevation.Level1`，跟`FoodItemCard`同一视觉语言（同一色阶+同一圆角，读起来像"同一家族"而不是另起一套卡片风格）
- 位置：TopAppBar正下方，左右外边距`space-4`（16dp），上下外边距`space-2`（8dp）
- 内部：`Row`，三等分（`weight(1f)`各一份），内边距`space-4`（16dp）
- 每一列：`Column`居中对齐，数字在上、标签在下，间距`space-1`（4dp）
  - 食材总数：数字`titleLarge`（22sp）+`onSurface`；标签`labelSmall`+`onSurfaceVariant`，文字"食材总数"
  - 即将过期：数字`titleLarge`+`extendedColors.warning`；标签同规格，文字"即将过期"
  - 已过期：数字`titleLarge`+`colorScheme.error`；标签同规格，文字"已过期"
- 取色逻辑完全复用`ExpiryBadge`已有的三态语义色（正常中性/即将过期琥珀/已过期红），不引入新颜色，用户扫一眼统计栏的颜色就能对应到列表里徽标的颜色，两处认知一致
- 三列之间不加分隔线（沿用"不滥用边框"原则，靠`weight`等分和留白区分），数字都是`maxLines=1`不换行，理论上手机冰箱食材量级在两位数以内，三位数以上属于极端情况，不做特别的字号自适应
- 无press/hover/disabled（纯展示，不可交互）
- 数字随数据变化时的更新：可选用150ms淡入淡出过渡（`AnimatedContent`），不是硬性要求，静态刷新也可接受

### 5.4 CategoryDropdownField（沿用v1组件，改为选填）

- label：`"分类"`（去掉v1的`"分类 *"`）
- placeholder：从`"请选择分类"`改为`"选填，可不选"`，用文案本身补足"去掉星号"损失的必填/选填信号，不完全依赖用户自己去猜星号的有无
- 去掉`isError`/`supportingText`错误态整条视觉路径，这个字段永远不进入错误态
- 展开态`DropdownMenu`、选中态高亮`secondaryContainer`、收起200ms淡出：不变
- 四态：默认/聚焦/press不变；不再有错误态；无disabled

### 5.5 ExpiryInputField（新增组件，替代v1内联在`AddEditFoodScreen`里的日期字段代码块）

沿用v1"过期日期 *"字段的必填地位和错误态视觉规则（2dp红边框+supportingText+trailing叹号图标），只是新增一层"日期/天数"双模式切换：

- 顶部：`SingleChoiceSegmentedButtonRow`（M3标准组件，两段："选日期"/"选天数"），宽度撑满，默认选中"选日期"（保持跟v1一致的默认行为，最小化对已有用户习惯的扰动）
  - 选中段：背景`secondaryContainer`，文字`onSecondaryContainer`；未选中段：背景`surface`，边框`outline`，文字`onSurfaceVariant`（配色逻辑跟FilterChip一致）
  - 形状：沿用M3 `SegmentedButton`库默认形状（两端圆角、中间直角分隔），这是该组件自身的标准规格，不是本项目"chip用8dp圆角矩形"那条规则要覆盖的对象，两者是不同组件、不同来源的形状约定，不冲突
- 下方内容区：按当前模式二选一显示，切换时用`AnimatedContent`过渡，200ms（复用`duration-normal`），淡入+轻微纵向位移，不用共享元素动画
  - "选日期"模式：v1原有只读字段+`DatePickerDialog`，视觉和交互完全不变
  - "选天数"模式：`Row`，左侧`OutlinedTextField`（宽度约96dp，`KeyboardType.Number`，placeholder"例如：3"）+ 右侧固定文案"天后到期"（`bodyLarge`，`onSurface`，垂直居中对齐输入框），两者间距`space-3`（12dp）
- 提交时换算：无论哪个模式为当前激活状态，都以该模式的值为准换算成绝对`expiryDate`（选天数模式：`LocalDate.now().plusDays(N)`），换算逻辑属于ViewModel/表单状态层，不是视觉规格，此处只标注UI侧需要暴露"当前激活模式+对应输入值"给上层
- 切换模式不清空另一模式已输入的值（用户切换回去不丢数据），只有当前激活模式的值参与提交
- 校验：当前激活模式对应的字段为空时触发错误态（日期未选 或 天数为空/非法），错误视觉跟v1的`OutlinedTextField`错误态规则完全一致（红边框+supportingText+叹号图标），未设上限校验（如天数超过365是否要提示），见待决策项
- 四态：默认/聚焦/错误 沿用v1 `OutlinedTextField`规则；无disabled

### 5.6 QuantityInputRow（新增组件，重量/数量选填字段）

- label：`"重量/数量"`，无"*"（选填字段的视觉标记就是没有星号，跟"分类"字段统一处理逻辑，全App对"选填"的表达方式保持一致）
- 布局：`Row`，两个子控件间距`space-3`（12dp）
  - 数字输入（`weight(2f)`，约占60%宽度）：`OutlinedTextField`，`KeyboardType.Decimal`（允许小数，如1.5斤），placeholder"例如：1.5"，默认值为空（不预填，用户要求"数字默认不预填"）
  - 单位下拉（`weight(1f)`，约占40%宽度）：外观复用`CategoryDropdownField`同一套视觉语言（只读`OutlinedTextField`+trailing chevron+`DropdownMenu`），选项：克/千克/斤/个/盒，**新增时默认选中"斤"**（用户明确要求默认预填单位），编辑已有条目时显示该条目存库的真实单位，不被默认值覆盖
- 两个子控件视觉高度一致，垂直顶对齐（label在各自内部，不需要外部再加一层共享label）
- 无校验：数字为空时单位字段不受影响，两者都为选填，不产生跨字段联动报错
- 四态：默认/聚焦 沿用`OutlinedTextField`标准规则；无错误态；无disabled

---

## 六、交互与动效规范（v2新增/变更部分，其余沿用v1第六节）

| 交互 | 时长 | 缓动 | 说明 |
|---|---|---|---|
| 抽屉展开/收起 | M3默认（约250ms） | M3标准 | 复用`duration-normal`量级，不新增独立token |
| 抽屉分类项选中切换 | 200ms | 标准 | 背景色`secondaryContainer`淡入，跟FilterChip选中动效手法一致 |
| 过期日期"选日期/选天数"模式切换 | 200ms | 标准 | `AnimatedContent`淡入+轻微纵向位移，复用`duration-normal` |
| 滑动删除弹回 | spring（M3默认） | — | 越过阈值松手后先弹回原位，不变的是v1"未达阈值弹回"这条规则，本次扩展到"达到阈值也先弹回" |
| 二次确认弹窗（AlertDialog） | 入场稍慢出场稍快 | M3标准 | 跟v1既有的"放弃更改"弹窗用同一套默认动效，不新起一套 |
| 确认删除后卡片收缩消失 | 200ms | `shrinkVertically`+`fadeOut` | 沿用v1既有实现，只是触发时机从"松手立即"改为"点击删除后" |
| 统计栏数字更新（可选） | 150ms | 淡入淡出 | 非硬性要求，静态刷新也可接受 |

Loading态、`prefers-reduced-motion`适配：沿用v1第六节说明，不变。

---

## 七、设计依据摘要

- 绿色改冷调提高饱和度、背景转薄荷白：直接对应用户"更清新时尚"的诉求，只调整既有色相内的明度饱和度和背景基调，没有新增色相，符合"色相不超过3种"的克制要求，也没有把改版范围扩大到字体/圆角/间距（用户没提，不擅自动）
- 分类抽屉配实景照片、"全部"用图标：抽屉是浏览+选择场景，值得投入更重的视觉（照片），跟列表卡片继续用轻量图标形成刻意的场景分工，这是v1就确立的"筛选chip vs 表单下拉外观不同因为场景不同"同一逻辑的延伸，不是不一致
- 统计栏数字尺寸克制在`titleLarge`（22sp）而非做成大屏数字：对应frontend-design自检项"避免Hero-metric数字大屏"这类AI套路，统计栏是列表页的辅助信息不是页面主体，尺寸和位置都刻意压低存在感
- 统计栏取色复用`ExpiryBadge`已有的三态语义色：避免同一App里出现两套表达"正常/警示/已过期"的颜色系统，维持认知一致性
- 删除二次确认去掉Snackbar撤销后补一句"删除后不可恢复"：AC明确要求二选一，本方案照办，但删除的不可逆性需要在UI里显式告知，不能让用户凭直觉猜测有没有后悔机会，这是工程正确性底线"关键操作需清晰反馈"的具体落实
- 分类降级选填后用placeholder文案"选填，可不选"补偿去掉星号损失的信号：星号的有无是一个容易被忽略的弱信号，用文案主动告知比让用户自己去对比"这个字段怎么没有星号"更可靠
- SegmentedButton形状沿用M3库默认（非本项目chip的8dp规则）：不同组件系统各自的标准形状，不强行统一成同一套shape token，符合"沿用既有组件默认，不做不必要的视觉发明"

---

## 八、待决策项

1. 统计栏三个数字是否应该随抽屉的分类筛选联动变化，本方案默认取"全局视角"（不随筛选变化，语义对应"整个冰箱的总览"），如果产品期望统计栏跟随当前筛选分类变化，需要明确并重新设计文案（比如"当前分类X件"）
2. 抽屉分类图片素材的具体来源、加载方式（本地drawable还是外部图片、是否需要懒加载）不在本方案范围，只规划了48x48dp、`shape.small`（8dp圆角矩形）、建议1:1裁切的占位要求
3. 去掉撤销Snackbar后，用户误删一个填了较多信息（分类+重量+特定过期日期）的条目将没有任何补救路径，本方案已按AC要求二选一执行，只在此提示这个产品层面的风险，供最终确认是否接受
4. v2新色值（尤其dark主题的`surfaceContainer`系列）是方向性推算，未经Material Theme Builder核验对比度，落地前建议跟v1一样走一遍官方工具复核
5. "选天数"模式下天数输入是否需要上限校验（比如超过365天要不要提示），本方案只做"非空+非负"校验，没有设上限，如需要请产品补充规则
6. App启动图标（mipmap/adaptive icon）本身的图形是否要跟着改名重新设计，本方案范围只覆盖App内界面（TopAppBar标题），图标视觉改版未涉及，需要另行评估

---

## 九、给code-writer的交付说明

**扫描来源**：项目`CLAUDE.md`（v2范围，含分类选填/重量数量字段schema/抽屉交互/统计栏/二次确认删除等规定）；`docs/ui-20260716-fridge-tracker.md`（v1方案，本文档的基础）；v1已实现代码（`FoodListScreen.kt`、`AddEditFoodScreen.kt`、`CategoryFilterChipRow.kt`、`Color.kt`，以及为完整理解现状额外读取的`FoodItemCard.kt`、`ExpiryBadge.kt`、`EmptyState.kt`、`CategoryDropdownField.kt`、`ExtendedColors.kt`、`Shape.kt`、`Type.kt`、`Theme.kt`、`FoodViewModel.kt`、`DateFormatUtils.kt`、`FoodItem.kt`、`FoodCategory.kt`）。

**保留不变（不要动）**：`NotoSansSC`字体体系、`Shapes`圆角阶梯、`space-*`间距token、动效时长token、`error`/`warning`语义色数值、`ExpiryBadge`组件、`EmptyState`组件、`FoodItemCard`除本文档5.1所列两处外的其余结构、`DatePickerDialog`的实现方式、AddEditFoodScreen里"放弃更改"确认弹窗的实现方式。

**修改文件**：
- `ui/theme/Color.kt`：按本文档2.1节表格更新全部`md_theme_light_*`和`md_theme_dark_*`常量值，token名不变
- `ui/components/CategoryDropdownField.kt`：label去掉"*"、placeholder改为"选填，可不选"、去掉`isError`/`supportingText`相关代码路径
- `ui/components/FoodItemCard.kt`：分类文字标签改条件渲染；滑动删除流程改为"松手先弹回→弹AlertDialog二次确认→确认后才播放收缩动画并删除"，具体文案和按钮见本文档5.1
- `ui/list/FoodListScreen.kt`：TopAppBar标题改"冰了个箱"、加leading菜单图标、整体用`ModalNavigationDrawer`包裹、去掉`deletedEvents`/`SnackbarHost`相关代码（撤销机制整体移除）、顶部插入`StatsBar`
- `ui/addedit/AddEditFoodScreen.kt`：去掉`categoryError`相关校验逻辑、把原有内联日期字段代码块换成新的`ExpiryInputField`组件调用、新增`QuantityInputRow`字段
- `res/values/strings.xml`：`app_name`改为"冰了个箱"（这是v2改名要求里唯一涉及但不在Compose UI代码里的一处，一并提醒）

**新增文件**（放入`ui/components/`，与项目既有目录约定一致）：
- `CategoryDrawerContent.kt`：抽屉内容，替代`CategoryFilterChipRow`在列表页的使用（`CategoryFilterChipRow.kt`本身是否删除由code-writer按项目习惯决定，本文档不强制）
- `StatsBar.kt`：统计栏
- `ExpiryInputField.kt`：过期日期双模式输入
- `QuantityInputRow.kt`：重量/数量选填字段

**不在本方案范围（数据层，已在CLAUDE.md规定，非UI设计工作）**：`FoodItem.category`改为可空、新增`quantity`/`quantityUnit`字段、`QuantityUnit`枚举定义、Room `fallbackToDestructiveMigration()`。这些交给code-writer按schema直接实现，`FoodViewModel.filteredFoods`现有的分类过滤逻辑（`if (category==null) items else items.filter{it.category==category}`）已经能正确处理"无分类食物只在'全部'下可见"这个行为，不需要额外改动。

**采纳的frontend-design审美方向**：
- 字体：沿用v1的CJK可读性优先取舍，不变
- 配色：绿色转冷调提饱和度、背景转薄荷白，避免陈词滥调的紫色渐变，同时保持v1已确立的3色相封顶
- 运动：新增的抽屉展开、日期模式切换动效都复用已有duration token，不新增高冲击时刻，动效投入依然克制
- 空间：统计栏尺寸和留白刻意压低存在感，不跟列表主内容抢视觉重心
- 背景：延续v1"靠surfaceContainer色阶+留白建立层次"的做法，本次只是把这套色阶整体调冷，没有引入纹理或渐变

**反AI套路自检结果**：统计栏三个大数字的形态跟"Hero-metric数字大屏"这个AI套路表面相似，已在设计依据里说明区别：字号克制在`titleLarge`（22sp，明显小于典型hero-metric的display级字号）、位置是列表页的辅助信息条而非页面主体、颜色复用既有语义色而非新配色方案，是功能必需（AC明确要求）而非装饰性炫技。字体、配色、布局的其余自检结论沿用v1（未使用Inter/Roboto泛滥字体、非紫色渐变白底套路、工具型App沿用列表+FAB可预测布局是有意识的效率优先选择）。

**实施注意事项**：
- 滑动删除新流程要注意`SwipeToDismissBoxState`的时序：越过阈值松手先让状态弹回`Settled`（`confirmValueChange`返回`false`），再单独用一个`showDeleteConfirm`状态弹`AlertDialog`，不要试图让卡片在对话框展示期间保持"揭开"状态，避免跟`reset()`的挂起特性产生竞态
- `ExpiryInputField`两种模式的输入值都要保留在各自状态里，不要因为切换模式互相清空
- 重量/数量字段的单位下拉，编辑已有条目时要回显库里存的真实单位，只有"新增"场景才默认"斤"
- 分类字段去掉错误态后，`AddEditFoodScreen`的`attemptSave()`里对应的`categoryError = category == null`那行要整段删除，不要留着不调用的死状态
- v2色值同v1一样是方向性建议，落地前建议用Material Theme Builder以新seed（`#159A66`）核验对比度，尤其是新的dark scheme
