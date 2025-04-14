<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-card style="margin-bottom: 5px;">
        <el-input
            v-model="data.title"
            style="width: 240px;margin-right: 5px"
            placeholder="Please input"
            clearable
        />
        <el-button :icon="Search" circle @click="load"/>
        <el-button :icon="Delete" circle @click="reset"/>
      </el-card>
      <el-card style="margin-bottom: 5px;">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="success" @click="delBatch">批量删除</el-button>
        <el-button type="info">Info</el-button>
        <el-button type="warning">Warning</el-button>
      </el-card>
      <el-card>
        <el-table :data="data.tabledata" stripe @selection-change="handleSelectionChange">
          <el-table-column type="selection"width="55" />
          <el-table-column label="标题" prop="title"></el-table-column>
          <el-table-column label="封面" prop="cover">
            <template #default="scope">
              <div style="display: flex; align-items: center; height: 100%;">
                <el-image  v-if="scope.row.cover" :src="scope.row.cover" :preview-src-list=[scope.row.cover] style="width: 70px; height: 50px;"></el-image>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="简介" prop="descr" show-overflow-tooltip></el-table-column>
          <el-table-column label="内容" prop="content">
            <template #default="scope">
              <el-button type="primary" @click = "view(scope.row.content)">查看内容</el-button>
            </template>
          </el-table-column>
          <el-table-column label="发布时间" prop="time"></el-table-column>
          <el-table-column label="操作" >
            <template #default="scope">
              <el-button @click="handleUpdate(scope.row)" type="primary" :icon="Edit" circle></el-button>
              <el-button @click="del(scope.row.id)" type="danger" :icon="Delete" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 15px">
          <el-pagination
              v-model:current-page="data.pageNum"
              v-model:page-size="data.pageSize"
              :page-sizes="[5, 10, 15, 20]"
              :size="data.pageSize"
              :disabled="disabled"
              :background="background"
              layout="total, sizes, prev, pager, next, jumper"
              :total="data.total"
              @size-change="load"
              @current-change="load"
          />
        </div>
      </el-card>
    </div>

    <!-- Dialog 弹窗 -->
<!--    :model 双向绑定-->
    <el-dialog title="文章信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="80px" style="padding-right: 30px">
        <div style="width: 100%; display: flex; justify-content: center; ">
          <el-form-item prop="cover" style="margin-left: -40px;">
            <el-upload
                class="cover-uploader"
                action="http://localhost:8080/files/upload"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
            >
              <img v-if="data.form.cover" :src="data.form.cover" class="cover" />
              <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </div>

        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入文章标题"/>
        </el-form-item>
        <el-form-item label="简介" prop="descr">
          <el-input type="textarea"  :rows="3" v-model="data.form.descr" autocomplete="off" placeholder="请输入文章简介"/>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div style="border: 1px solid #ccc">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
            />
          </div>  </el-form-item>

      </el-form>

      <!-- footer 插槽 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>

<!--    Dialog&#45;&#45;上传文件-->
    <el-dialog title = "内容" v-model="data.viewVisible" width="500px" :close-on-click-modal="false" destroy-on-close>
      <div class = "editor-content-view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click = "data.viewVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { onBeforeUnmount, shallowRef, onMounted } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import { reactive, ref } from "vue";
import {Search, Delete, Edit} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref();

const data = reactive({
  title: null,
  tabledata: [],
  content:null,
  viewVisible:false,
  pageNum: 1,
  pageSize: 10,
  total: 47,
  formVisible: false,
  form: [],
  ids:[],
});

const baseUrl = 'http://localhost:8080';
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF:{}}

editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file',
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

const view = (content) =>{
  data.content = content
  data.viewVisible = true
}
//当上传成功后，handleCoverSuccess 被调用，并接收服务器的响应数据。
//该函数将新头像的 URL 更新到 data.form.avatar 中。
//由于 data.form.avatar 在模板中被用作头像图片的 src，更新后头像会实时显示在页面上。
const handleCoverSuccess = (res) =>{
  data.form.cover = res.data
}

// 异步请求
const load = () => {
  console.log("Searching for title: ", data.title); // 打印输入的内容
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then((res) => {
    console.log("返回的数据：", res);  // 查看返回的完整数据结构
    data.tabledata = res.data.list;
    data.total = res.data.total;
  });
};
load();

const reset = () => {
  data.title = null;
  load();
};

const handleAdd = () => {
  data.formVisible = true;
  data.form = {};   //清空，以免脏数据
};

const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row));    //深拷贝一个新的对象 用于编辑

  data.formVisible = true;
};

const save = () =>{   //在一个保存方法里做两个操作，一个是新增，一个是编辑
  data.form.id ? update() : add()
}

const add = () =>{
  request.post('/article/add',data.form).then((res) => {     //新增的对象没有id
    if(res.code === '200'){
      data.formVisible = false;
      ElMessage.success('操作成功')
      load()    //新增后加载最新的数据
    }else{
      ElMessage.error(res.msg);
    }
  })
}
const update = () =>{
  request.put('/article/update',data.form).then((res) => {    //编辑的对象有id
    if(res.code === '200'){
      data.formVisible = false;
      ElMessage.success('操作成功')
      load()    //新增后加载最新的数据
    }else{
      ElMessage.error(res.msg);
    }
  })
}

const del = (id) =>{
  ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {type: 'warning'}).then(() => {
    request.delete(`/article/deleteById/` + id).then((res) => {
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load()    //删除后加载最新的数据
      }else{
        ElMessage.error(res.msg);
      }
    })
  }).catch()
}
// 函数是 Promise 式的，如果用户点击了 “确认”，就会进入 .then(...) 逻辑；
// 如果点击了 “取消”，就会进入 .catch(...)（此处 .catch() 是空的，表示忽略取消操作）。




// rows，它代表 当前选中的所有行数据。当用户在表格中勾选或取消勾选某一行时，
// selection-change 事件会触发，并将选中的行数据作为参数传递给这个方法。
const handleSelectionChange =(rows)=>{      //返回所有选中的行对象数组
  console.log(rows)
  //从选中的行数组中，取出所有行的id组成一个新的数组
  data.ids = rows.map(row => row.id)
  console.log(data.ids)
}

const delBatch=() =>{
  if(data.ids.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {type: 'warning'}).then(() => {
    request.delete('/article/deleteBatch', {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()    //删除后加载最新的数据
      } else {
        ElMessage.error(res.msg);
      }
    })

  }).catch()
}
</script>

<style scoped>
.cover-uploader .cover {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.cover-uploader .el-upload {
  border-radius: 50%;
  border: 1px dashed var(--el-border-color);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.cover-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>