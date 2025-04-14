<template>
  <div style="display:flex;grid-gap: 10px">
    <!-- ECharts 容器 -->
    <el-card>
      <div id="bar" style="padding: 10px; width: 610px; height: 400px;"></div>
    </el-card>
    <el-card>
      <div id="line" style="padding: 10px;width: 610px; height: 400px;"></div>
    </el-card>
  </div>
  <div style="display:flex;grid-gap: 10px;padding-top: 10px">
    <el-card >
      <div id="pie" style="padding: 10px;width: 610px; height: 400px;"></div>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, reactive} from 'vue'
import * as echarts from 'echarts'
import request from "@/utils/request.js";

const data = reactive({

});

// 定义 option 常量（而不是函数），或者用函数也行，但一定要 return！
const optionBarChart = {
  title: {
    text: '各部门员工数量'
  },
  tooltip: {},
  legend: {
    data: ['人数']
  },
  xAxis: {
    type: 'category', // 一定要加这个
    data: []
  },
  yAxis: {
    type: 'value' // 一定要加这个
  },
  series: [
    {
      name: '人数',
      type: 'bar',
      data: [],
      itemStyle:{
        normal:{
          color:function(params){
            //let块级作用域 不允许重复声明
            let colors = ['#5470c6','#91cc75','#fac858','#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'];
            //params.dataIndex 数组下标
            return colors[params.dataIndex % colors.length]
          }
        }
      }
    }
  ]
}

const optionLineChart = {
  title: {
    text: '近7天发布的文章数量'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name:'发布数量',
      data: [],
      type: 'line',
      smooth: true,
    }
  ]
};

const optionPieChart = {
  title: {
    text: '各部门员工数量比例',
    subtext: 'Fake Data',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '员工数量',
      type: 'pie',
      radius: '50%',
      data: [],
      label:{
        formatter: '{b}:{@2012} ({d}%)',
      },
      center: ['50%', '55%'],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

//onMounted 表示页面的所有dom元素都初始化完成了
onMounted(() => {
  //基于准备好的dom，初始化echart实例
  const barChart = echarts.init(document.getElementById('bar'))
  request.get('/barData').then(res => {
    optionBarChart.xAxis.data = res.data.department   //department和 count都是从后端接口拿到的数据
    optionBarChart.series[0].data = res.data.count
    barChart.setOption(optionBarChart)
  })



  const lineChart = echarts.init(document.getElementById('line'))
  request.get('/lineData').then(res => {
    optionLineChart.xAxis.data = res.data.date   //department和 count都是从后端接口拿到的数据
    optionLineChart.series[0].data = res.data.count
    lineChart.setOption(optionLineChart)
  })

  const pieChart = echarts.init(document.getElementById('pie'))
  request.get('/pieData').then(res => {
    optionPieChart.series[0].data = res.data
    pieChart.setOption(optionPieChart)
  })



})
</script>

<style scoped>
</style>
