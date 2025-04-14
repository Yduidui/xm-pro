<template>
  <div class="home-container">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="data-card" shadow="hover">
          <div class="card-header">
            <el-icon :size="40" color="#67C23A"><User /></el-icon>
            <span class="card-title">员工总数</span>
          </div>
          <div class="card-content">
            <span class="card-value">{{ data.employeeCount }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card" shadow="hover">
          <div class="card-header">
            <el-icon :size="40" color="#409EFF"><DataBoard /></el-icon>
            <span class="card-title">数据总量</span>
          </div>
          <div class="card-content">
            <span class="card-value">{{ data.totalData }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card" shadow="hover">
          <div class="card-header">
            <el-icon :size="40" color="#E6A23C"><Warning /></el-icon>
            <span class="card-title">异常数据</span>
          </div>
          <div class="card-content">
            <span class="card-value">{{ data.errorData }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card" shadow="hover">
          <div class="card-header">
            <el-icon :size="40" color="#F56C6C"><Timer /></el-icon>
            <span class="card-title">今日更新</span>
          </div>
          <div class="card-content">
            <span class="card-value">{{ data.todayUpdates }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="hover">
      <template #header>
        <div class="table-header">
          <span class="table-title">员工数据</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增员工</el-button>
        </div>
      </template>
      <el-table :data="data.employeeList" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="position" label="职位" width="120" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="电话" width="150" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue';
import { User, DataBoard, Warning, Timer, Plus } from '@element-plus/icons-vue';
import request from '@/utils/request.js';

// 初始化响应式数据
const data = reactive({
  employeeList: [],
  employeeCount: 0,
  totalData: 0,
  errorData: 0,
  todayUpdates: 0
});

// 获取员工数据
const fetchEmployeeData = async () => {
  try {
    const res = await request.get('employee/selectAll');
    data.employeeList = res.data;
    data.employeeCount = res.data.length;
  } catch (error) {
    console.error('获取员工数据失败:', error);
  }
};

// 模拟其他数据
const fetchOtherData = () => {
  data.totalData = 1000;
  data.errorData = 12;
  data.todayUpdates = 5;
};

// 新增员工
const handleAdd = () => {
  console.log('新增员工');
};

// 编辑员工
const handleEdit = (row) => {
  console.log('编辑员工:', row);
};

// 删除员工
const handleDelete = (row) => {
  console.log('删除员工:', row);
};

// 页面加载时获取数据
onMounted(() => {
  fetchEmployeeData();
  fetchOtherData();
});
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.data-card {
  margin-bottom: 20px;
  text-align: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  margin-left: 10px;
}

.card-content {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.table-card {
  margin-top: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-title {
  font-size: 18px;
  font-weight: bold;
}
</style>