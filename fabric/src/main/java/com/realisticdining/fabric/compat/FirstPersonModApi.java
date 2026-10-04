package com.realisticdining.fabric.compat;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * First-person Model 模组 API 反射调用工具类。
 * 通过反射调用 dev.tr7zw.firstperson.api.FirstPersonAPI，避免硬依赖。
 */
public final class FirstPersonModApi {
    
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean lookupDone = false;
    private static Method isEnabledMethod = null;
    private static Method setEnabledMethod = null;
    
    private FirstPersonModApi() {
    }
    
    /**
     * 查找并缓存 FirstPersonAPI 的方法引用。
     * @return true 如果 API 可用且方法已成功获取
     */
    public static boolean findApi() {
        if (lookupDone) {
            return isEnabledMethod != null && setEnabledMethod != null;
        }
        lookupDone = true;
        
        // 检查 First-person Model 模组是否已加载
        if (!FabricLoader.getInstance().isModLoaded("firstperson")) {
            return false;
        }
        
        try {
            Class<?> api = Class.forName(
                "dev.tr7zw.firstperson.api.FirstPersonAPI",
                false,
                FirstPersonModApi.class.getClassLoader()
            );
            isEnabledMethod = api.getMethod("isEnabled");
            setEnabledMethod = api.getMethod("setEnabled", boolean.class);
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            LOGGER.warn("FirstPersonMod API was not found.", exception);
            return false;
        }
    }
    
    /**
     * 检查 First-person Model 是否已启用。
     * @return true 如果 First-person Model 当前启用
     */
    public static boolean isFirstPersonModEnabled() {
        if (!findApi()) {
            return false;
        }
        try {
            return (boolean) isEnabledMethod.invoke(null);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            LOGGER.warn("Could not read FirstPersonMod state.", exception);
            return false;
        }
    }
    
    /**
     * 设置 First-person Model 的启用状态。
     * @param enabled true 启用，false 禁用
     */
    public static void setFirstPersonModEnabled(boolean enabled) {
        if (!findApi()) {
            return;
        }
        try {
            setEnabledMethod.invoke(null, enabled);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            LOGGER.warn("Could not change FirstPersonMod state.", exception);
        }
    }
}
