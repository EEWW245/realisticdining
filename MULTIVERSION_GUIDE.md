# Realistic Dining - Multi-Version Build Guide

## 目录结构

```
realisticdining-fabric-neoforge/
├── common/                    # 1.21.1 版本通用代码
├── fabric/                    # 1.21.1 Fabric 版本
├── neoforge/                  # 1.21.1 NeoForge 版本
├── versions/
│   └── 1.20.1/               # 1.20.1 版本所有文件
│       ├── common/           # 1.20.1 通用代码
│       ├── fabric/           # 1.20.1 Fabric 版本
│       ├── neoforge/         # 1.20.1 NeoForge 版本
│       └── build.gradle      # 1.20.1 构建配置
└── build.gradle              # 根构建配置
```

## 构建说明

### 构建 1.21.1 版本

```bash
# 构建所有 (Fabric + NeoForge)
.\gradlew.bat build

# 只构建 Fabric
.\gradlew.bat :fabric:build

# 只构建 NeoForge
.\gradlew.bat :neoforge:build

# 清理并构建
.\gradlew.bat clean build
```

**输出位置:**
- `fabric/build/libs/realisticdining-fabric-1.0.3.jar`
- `neoforge/build/libs/realisticdining-neoforge-1.0.3.jar`

### 构建 1.20.1 版本

```bash
# 进入 1.20.1 目录
cd versions\1.20.1

# 构建所有 (Fabric + NeoForge)
.\gradlew.bat build

# 只构建 Fabric
.\gradlew.bat :fabric:build

# 只构建 NeoForge
.\gradlew.bat :neoforge:build

# 清理并构建
.\gradlew.bat clean build
```

**输出位置:**
- `versions/1.20.1/fabric/build/libs/realisticdining-fabric-1.0.3.jar`
- `versions/1.20.1/neoforge/build/libs/realisticdining-neoforge-1.0.3.jar`

## 版本配置

### 1.21.1 版本
- Minecraft: 1.21.1
- Fabric Loader: 0.16.10
- Fabric API: 0.102.0+1.21.1
- NeoForge: 21.1.65
- Architectury: 13.0.8

### 1.20.1 版本
- Minecraft: 1.20.1
- Fabric Loader: 0.16.9
- Fabric API: 0.92.2+1.20.1
- NeoForge: 47.3.12
- Architectury: 9.2.14

## 开发工作流

### 添加新功能

1. **在 1.21.1 上开发测试**
   ```bash
   # 在根目录开发
   # 修改 common/, fabric/, neoforge/ 中的代码
   .\gradlew.bat build
   ```

2. **移植到 1.20.1**
   ```bash
   # 复制修改的文件到 versions/1.20.1/
   # 确保 API 兼容
   cd versions\1.20.1
   .\gradlew.bat build
   ```

3. **同步 Bug 修复**
   - 两个版本的 Bug 修复要及时同步
   - 使用 Git 管理版本控制

### 发布版本

发布前确保：
- [ ] 两个版本都构建成功
- [ ] 版本号已更新
- [ ] 更新日志已编写
- [ ] 在两个版本都测试过

## 依赖管理

### 1.21.1 依赖
在根目录的 `gradle.properties` 中修改

### 1.20.1 依赖
在 `versions/1.20.1/gradle.properties` 中修改

**注意:** 两个版本的依赖要分别管理，确保版本兼容！

## 常见问题

### Q: 为什么使用子目录结构而不是 Git 分支？
A: 子目录结构更清晰，两个版本可以同时在 IDE 中打开，方便对比和同步代码。

### Q: 如何同步两个版本的代码？
A: 手动复制文件或使用脚本。建议先在一个版本开发测试，稳定后移植到另一个版本。

### Q: 可以只维护一个版本吗？
A: 可以。如果只维护 1.21.1，忽略 `versions/1.20.1/` 目录即可。

## 森罗物语厨房联动

本模组与**森罗物语厨房 (Kaleidoscope Cookery)** 深度联动。

### 依赖版本
- **1.21.1**: 从 CurseForge 自动下载
- **1.20.1**: 从 CurseForge 自动下载 (File ID: 5589842)

如果联动出现问题，检查：
1. 森罗物语厨房是否安装
2. 版本是否匹配
3. 依赖版本是否正确

## 技术支持

-  Issues: GitHub Issues
-  社区：Discord / QQ 群

---

**Realistic Dining** - 更真实的用餐体验 🍳
