<template>
  <div>
    <RouterLink to="/Test">跳转到Test.vue</RouterLink>
  </div>
  <div>
    <el-button type="primary" @click="router.push('/Test')">push跳转新页面</el-button>
    <el-button type="primary" @click="router.replace('/Test')">replace跳转新页面</el-button>
  </div>
  <div style="background-color: aqua">
    hello world
  </div>

  <div>
    {{a+b}}
  </div>

  <div>                       //渲染数据
    {{data.a}}
    {{data.b}}
  </div>

  <div>
    <input v-model = "data.a">
  </div>
  <div>
    <span style="background-color: red" v-if="data.name === 'shabi'">woshidashabi</span>
    <span style="background-color: yellow" v-if="data.name === 'benbi'">woshidabenbi</span>
    <span style="background-color: deeppink" v-else>woshidasss</span>
  </div>

  <div>
    <div @click="show(item)" style="width: 100px; height: 100px;background-color: green" v-for="item in data.arr">{{item}}</div>

  </div>

  <div>
    <!--    <button v-on:click="click">点我加好运</button>-->
    <button @click="click">点我加好运</button>
  </div>

  <div :style="data.box">

  </div>
  <div>
    <el-button @click="click">Default</el-button>
    <el-button type="primary">Primary</el-button>
    <el-button type="success">Success</el-button>
    <el-button type="info">Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button type="danger">Danger</el-button>
  </div>

  <div>
    <el-icon :size="20">
      <Edit />
    </el-icon>
    <el-button type="danger" :icon="Delete" circle />
  </div>

  <div class="flex gap-4 mb-4">
    <span>Using attributes</span>
    <el-input
        v-model="input1"
        style="width: 240px"
        placeholder="Pick a date"
        :suffix-icon="Calendar"
    />
    <el-input
        v-model="input2"
        style="width: 240px"
        placeholder="Type something"
        :prefix-icon="Search"
    />
  </div>
</template>

<script setup>
import {reactive, ref, onMounted} from "vue"; //导包
import {Delete, Calendar, Search} from "@element-plus/icons-vue"
import router from "@/router/index.js";


const a = ref(1);
const b = ref(2);
const data = reactive({  //定义数据
  a:123,
  b:"woshidshabi",
  name:'',
  arr :[1,2,0],
  box:{
    width:'100px',
    height:'100px',
    backgroundColor:'red'
  }

})

const click = () => {
  alert("haoyunjia1")
}

const show = (value) => {
  alert(value)
}

// onMounted(() => {
//   alert("11111111")
// })
</script>