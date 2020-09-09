<template>
  <div class="app-container">
    <el-row :gutter="10">
      <!--分类数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="wordsName" placeholder="请输入词汇分类名称" clearable size="large" prefix-icon="el-icon-search" style="margin-bottom: 20px" />
        </div>
        <div class="head-container">
          <el-tree :data="WordsOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode" ref="tree" default-expand-all @node-click="handleNodeClick" />
        </div>
      </el-col>
      <!--单词数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="90px">          
          <!-- <el-form-item>                             
          <el-button icon="el-icon-arrow-down" size="large" @click="showToggle"></el-button>    
          </el-form-item>                                 -->
          <el-form-item label="单词" prop="oneWordName">
            <el-input v-model="queryParams.oneWordName" placeholder="请输入单词关键字搜索" clearable size="large" style="width: 835px" @keyup.enter.native="handleQuery" />
          </el-form-item> 
          <el-form-item>
            <el-button icon="el-icon-arrow-down" size="large" @click="showToggle"></el-button> 
            <el-button type="cyan" icon="el-icon-search" size="large" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="large" @click="resetQuery">重置</el-button>
          </el-form-item>
          <div style="display:none" ref="findList">               
          <el-form-item label="描述" prop="descriptionNow">
            <div><el-input v-model="queryParams.descriptionNow" placeholder="请输入内容描述关键字搜索" clearable size="large" style="width: 835px" @keyup.enter.native="handleQuery" /></div>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="单词状态" clearable size="large" style="width: 368px">
              <el-option v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
            </el-select>
          </el-form-item>                   
          <el-form-item label="创建时间">
            <el-date-picker v-model="dateRange" size="large" style="width: 368px" value-format="yyyy-MM-dd" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
          </el-form-item>
          </div>  
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['vocabulary:build:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['vocabulary:build:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['vocabulary:build:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport" v-hasPermi="['vocabulary:build:import']">导入</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['vocabulary:build:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="oneWordList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="单词编号" align="center" prop="oneWordId" />
          <el-table-column label="单词名称" align="center" prop="oneWordName" :show-overflow-tooltip=true />
          <el-table-column label="所属分类" align="center" prop="words.wordsName" :show-overflow-tooltip=true />
          <el-table-column label="详情描述" align="center" prop="descriptionNow" width="120" :show-overflow-tooltip=true />
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>          
          <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['vocabulary:build:query']">修改</el-button>
              <el-button v-if="scope.row.userId !== 1" size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['vocabulary:build:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="单词名称" prop="oneWordName">
              <el-input v-model="form.oneWordName" placeholder="请输入单词名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分类" prop="wordsId">
              <treeselect v-model="form.wordsId" :options="WordsOptions" :disable-branch-nodes="true" :show-count="true" placeholder="请选择上级分类" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
           <el-col :span="24">
            <el-form-item label="详情描述">
              <el-input v-model="form.descriptionNow" type="textarea" placeholder="请输入内容" :rows="16" ></el-input>
            </el-form-item>          
          </el-col>           
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in statusOptions" :key="dict.dictValue" :label="dict.dictValue">{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的单词数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getToken } from "@/utils/auth";
  import { 
    treeselect,
    listOneWord,
    getOneWord,
    updateOneWord,
    addOneWord,
    delOneWord,
    exportOneWord,
    importTemplate,
    changeOneWordStatus 
  } from "@/api/vocabulary/vocabulary";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import Editor from '@/components/Editor';

  export default {
    name: "OneWord",
    components: { Treeselect, Editor},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 单个单词数据
        oneWordList: null,
        // 弹出层标题
        title: "",
        // 分类树选项
        WordsOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 单词分类名称
        wordsName: undefined,
        // 默认密码
        initPassword: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 性别状态字典
        sexOptions: [],
        // 岗位选项
        postOptions: [],
        // 角色选项
        roleOptions: [],
        // 表单参数
        form: {},
        //打开搜索更多选项
        isShow:true,
        defaultProps: {
          children: "children",
          label: "label",
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的单词数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/oneword/build/importData",
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          oneWordName: undefined,
          descriptionNow: undefined,
          status: undefined,
          wordsId: undefined,
        },
        // 表单校验
        rules: {
          oneWordName: [
            { required: true, message: "单词名称不能为空", trigger: "blur" },
          ],
          descriptionNow: [
            { required: true, message: "详情描述不能为空", trigger: "blur" },
          ],
          wordsId: [
            { required: true, message: "单词所属分类不能为空", trigger: "blur" },
          ],
        },
      };
    },
    watch: {
      // 根据名称筛选分类树
      wordsName(val) {
        this.$refs.tree.filter(val);
      },
    },
    created() {
      this.getList();
      this.getTreeselect();
      this.getDicts("sys_normal_disable").then((response) => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 打开搜索DIV隐藏选项 */
      showToggle() {
        this.isShow = !this.isShow
          if(this.isShow){
            this.$refs.findList.style.display = "none"
          }else{
             this.$refs.findList.style.display = "block"
          }
      },
      /** 查询单个单词列表 */
      getList() {
        this.loading = true;
        listOneWord(this.addDateRange(this.queryParams, this.dateRange)).then(
          (response) => {
            this.oneWordList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询分类下下拉树结构 */
      getTreeselect() {
        treeselect().then((response) => {
          this.WordsOptions = response.data;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.wordsId = data.id;
        this.getList();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm(
          '确认要"' + text + '""' + row.oneWordName + '"单词吗?',
          "警告",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(function () {
            return changeOneWordStatus(row.oneWordId, row.status);
          })
          .then(() => {
            this.msgSuccess(text + "成功");
          })
          .catch(function () {
            row.status = row.status === "0" ? "1" : "0";
          });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          oneWordId:undefined,
          oneWordName: undefined,
          wordsId: undefined,
          descriptionNow: undefined,
          status: "0",
          remark: undefined,
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map((item) => item.oneWordId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getTreeselect();
        getOneWord().then((response) => {
          this.open = true;
          this.title = "添加单词";
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.getTreeselect();
        const oneWordId = row.oneWordId|| this.ids;
        getOneWord(oneWordId).then((response) => {
          console.log("response.data"+response.data)
          this.form = response.data;
          this.open = true;
          this.title = "修改单词";
          this.form.password = "";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            if (this.form.oneWordId != undefined) {
              updateOneWord(this.form).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addOneWord(this.form).then((response) => {
                if (response.code === 200) {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                }
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oneWordIds = row.oneWordId || this.ids;
              console.log("--oneWordIds------"+oneWordIds)
        this.$confirm(
          '是否确认删除单词编号为"' + oneWordIds + '"的数据项?',
          "警告",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(function () {
            return delOneWord(oneWordIds);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
          .catch(function () {});
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm("是否确认导出所有单词数据项?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(function () {
            return exportOneWord(queryParams);
          })
          .then((response) => {
            console.log("response.msg--------------->"+response.msg);
            this.download(response.msg);
          })
          .catch(function () {});
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "单词导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        importTemplate().then((response) => {
          this.download(response.msg);
        });
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      },
    },
  };
</script>
<style>
</style>
