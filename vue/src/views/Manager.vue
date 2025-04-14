<template>
  <!-- 头部开始 -->
  <div style="height: 60px; background-color: skyblue; display: flex; align-items: center;">
    <div style="display:flex;width: 200px; padding-left: 20px;align-items: center;">
      <img style="width: 40px;margin-right: 5px" src="@/assets/logo.png">
      <span style="font-size: 20px; color: #f5f7fa;">后台管理系统</span>
    </div>

    <div style="flex: 1"></div>

    <div style="display:flex;width: fit-content;padding-right: 15px;align-items: center;">
      <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" style="width: 40px;border-radius: 50%;height:40px">
      <span style="color: #f5f7fa;margin-left: 6px;font-size: 15px"> {{ data.user ? data.user.name : '未知用户' }}</span>
    </div>
  </div>
  <!-- 头部结束 -->

  <!-- 下面开始 -->
  <div style="display: flex">
    <!-- 左侧导航栏开始 -->
    <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
      <el-menu router :default-active="currentRoutePath" style="border: 0px">
        <el-menu-item index="/manager/home">
          <el-icon><HomeFilled /></el-icon>
          <span>系统首页</span>
        </el-menu-item>

        <el-menu-item index="/manager/data">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>

        <el-menu-item index="/manager/article">
          <el-icon><Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>

        <el-menu-item index="/manager/department">
          <el-icon><OfficeBuilding /></el-icon>
          <span>部门管理</span>
        </el-menu-item>


        <el-sub-menu index="1">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/manager/employee">员工信息</el-menu-item>
          <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/manager/personalinfo">
          <el-icon><User /></el-icon>
          <span>个人信息</span>
        </el-menu-item>

        <el-menu-item index="/manager/changePassword">
          <el-icon><User /></el-icon>
          <span>修改密码</span>
        </el-menu-item>

        <el-menu-item @click="logout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出登录</span>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 左侧导航栏结束 -->

    <!-- 右侧主题区域开始 -->
    <div style="flex: 1; width: 0; background-color: #f5f7fa; padding: 10px">
      <router-view @updateUser = "updateUser"></router-view>
    </div>
    <!-- 右侧主题区域结束 -->
  </div>
  <!-- 下面结束 -->
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { DataAnalysis, HomeFilled, SwitchButton, User } from '@element-plus/icons-vue';

// 获取当前的路由路径
const route = useRoute();
const currentRoutePath = ref(route.path);
//
// 解析 localStorage 中的用户信息
let user = null;
try {
  user = JSON.parse(localStorage.getItem('xm-pro-user'));
} catch (error) {
  console.error('解析用户信息失败:', error);
}

const data = reactive({
  user: user || { name: '未知用户' }
});


// 路由变化时更新路径
onMounted(() => {
  currentRoutePath.value = route.path;
});

const logout = () =>{
  localStorage.removeItem('xm-pro-user');   //清除登录用户的用户缓存数据
  location.href = '/login';   //退出到登录页面
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-pro-user'));
}
</script>

<style scoped>
/* 可根据需求在此添加自定义样式 */
</style>