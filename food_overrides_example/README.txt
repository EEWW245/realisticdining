Realistic Dining 食物配置数据包示例
======================================

【适用版本】
- Minecraft 1.21.1（Fabric / NeoForge）
- Minecraft 1.20.1（Fabric / Forge）
- 4 平台共用同一份数据包，无需修改

【支持范围】
- 仅支持覆盖「饮料/零食」（含瓶装饮料、罐头、薯片、辣条、尖叫饮料等）
- 不支持覆盖菜品（米饭、筷子、辣子鸡等菜品食物）——菜品饱食度/饱和度/buff 由模组代码硬编码

【数据包用法】
1. 把整个 food_overrides_example 文件夹复制到存档目录的 datapacks/ 下
   （如 .minecraft/saves/你的存档/datapacks/food_overrides_example）
2. 进入游戏，输入 /reload 命令重新加载数据包
3. 查看服务端日志，应能看到：
   [Realistic Dining] Loaded food overrides: 2 drinks
4. 数据包里的覆盖立即生效

【JSON 文件路径规则】
- 饮料/零食（含罐头、辣条、尖叫饮料、薯片等）：
    data/realisticdining/foods/drinks/<drinkId>.json
  例：data/realisticdining/foods/drinks/scream.json
  drinkId 就是物品的注册名（不含命名空间），如 scream、latiao、canned_beef、canned_honey_peach、potato_chips、mineral_water

【JSON 字段说明】

饮料/零食 JSON 字段：
{
  "max_uses":     最大使用次数（瓶装饮料 2，其他 1）
  "nutrition":    动画结束时一次性恢复的饱食度
  "saturation":   动画结束时一次性恢复的饱和度
  "effects":      [{"effect": "minecraft:regeneration", "duration": 200, "amplifier": 0}]
                  effect: 原版或模组 effect ID
                  duration: 持续时间（tick，20 tick = 1 秒）
                  amplifier: 等级（0 = I 级，1 = II 级）
  "hunger_cues":  动画中分时段触发饱食度的秒数列表（如 [3.0, 4.0, 5.0, 6.0]）
                  仅薯片等"一口一口吃"的零食会用到，普通饮料/罐头留空数组 []
  "clear_harmful": true/false，是否清除一切负面效果（类似原版牛奶，奶啤酒/豆浆默认为 true）
}

【覆盖语义】
- 全量替换：JSON 中的字段会替换代码默认值，缺失的字段不会保留默认值，而是按 JSON 字段缺失时的回退默认值处理
  （如不写 effects，等同于 effects=[]；不写 max_uses，等同于 max_uses=1）
- 数据包缺失或 JSON 解析失败时，模组行为完全不变（不会崩游戏）

【可用的原版 effect ID 列表】
- minecraft:regeneration     生命恢复
- minecraft:speed            迅捷
- minecraft:strength         力量（= minecraft:damage_boost，两者等价）
- minecraft:haste            快速挖掘（= minecraft:dig_speed）
- minecraft:resistance       抗性提升
- minecraft:fire_resistance  抗火
- minecraft:water_breathing  水下呼吸
- minecraft:night_vision     夜视
- minecraft:invisibility    隐身
- minecraft:jump_boost       跳跃提升
- minecraft:slow_falling     缓降
- minecraft:absorption       伤害吸收
- minecraft:health_boost     生命提升
- minecraft:saturation        饱和（瞬间回饱食度）

【示例覆盖说明】
1. drinks/scream.json         把尖叫饮料改为：8 饱食度 + 1.0 饱和 + 生命恢复 II 30s + 力量 II 60s + 迅捷 II 30s
2. drinks/canned_beef.json   把牛肉罐头改为：10 饱食度 + 1.0 饱和 + 力量 I 30s + 抗性提升 I 30s

【薯片类零食覆盖示例（不在数据包中，仅供参考）】
薯片默认走「分时段触发饱食度」机制：max_uses=1, nutrition=0, saturation=0.25, hunger_cues=[3.0, 4.0, 5.0, 6.0]
每个 cue 在动画播放到该秒数时 +2 点饱食度、+1 点饱和度。
若想覆盖薯片配置，例：
data/realisticdining/foods/drinks/potato_chips.json
{
  "max_uses": 1,
  "nutrition": 0,
  "saturation": 0.3,
  "effects": [],
  "hunger_cues": [3.0, 4.0, 5.0, 6.0],
  "clear_harmful": false
}

【注意事项】
- 数据包缺失或 JSON 解析失败时，模组行为完全不变（不会崩游戏）
- 修改 JSON 后必须 /reload 才能生效
- 不要在 JSON 中写注释（_comment 字段是示例自创，会被忽略，不报错）
- effect ID 必须有效，否则模组会打印警告并跳过该 effect
- 数据包仅对饮料/零食生效，菜品（米饭/筷子/辣子鸡等）的饱食度、饱和度、buff 不受数据包影响
