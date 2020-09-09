<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" align="center">
      <el-form-item label="" prop="wordsName">
        <el-input
          v-model="queryParams.wordsName"
          placeholder="请输入要搜索的分类"
          clearable
          size="large"         
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="large" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="large" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['vocabulary:handle:list']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="wordsList"
      row-key="wordsId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="wordsName" label="所属分类名称" width="400"></el-table-column>
      <el-table-column prop="wordsId" label="所属分类编号"  align="center" width="140"></el-table-column>      
      <el-table-column prop="orderNum" label="排序" align="center" width="70"></el-table-column>
      <el-table-column label="创建时间"  prop="createTime" align="center" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>      
      <el-table-column prop="remark" label="备注" align="center" width="400" :show-overflow-tooltip=true> 
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['vocabulary:handle:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['vocabulary:handle:add']"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['vocabulary:handle:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改分类对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="分类名称" prop="wordsName">
              <el-input v-model="form.wordsName" placeholder="请输入添加的词汇名称" clearable="true" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.parentId !== 0">
            <el-form-item label="上级分类" prop="parentId">
              <treeselect v-model="form.parentId" :options="wordsOptions" :normalizer="normalizer" placeholder="选择上级分类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" :autosize="{ minRows: 3, maxRows: 10 }" clearable="true" ></el-input>
            </el-form-item>
          </el-col>             
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listWords,getWords,listWordsExcludeChild,updateWords,addWords,delWords} from "@/api/vocabulary/vocabulary";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Words",
    components: { Treeselect },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 表格树数据
        wordsList: [],
        // 分类树选项
        wordsOptions: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          wordsName: undefined,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          parentId: [
            { required: true, message: "上级分类不能为空", trigger: "blur" }
          ],
          wordsName: [
            { required: true, message: "分类名称不能为空", trigger: "blur" }
          ],
          orderNum: [
            { required: true, message: "菜单顺序不能为空", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询词汇分类列表 */
      getList() {
        this.loading = true;
        listWords(this.queryParams).then(response => {
          this.wordsList = this.handleTree(response.data, "wordsId");
          this.loading = false;
        });
      },
      /** 转换分类数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.wordsId,
          label: node.wordsName,
          children: node.children
        };
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          wordsId: undefined,
          parentId: undefined,
          wordsName: undefined,
          orderNum: 0,
          remark:undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        if (row != undefined) {
          this.form.parentId = row.wordsId;
        }
        this.open = true;
        this.title = "添加单词分类";
        listWords().then(response => {
          this.wordsOptions = this.handleTree(response.data, "wordsId");
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        getWords(row.wordsId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改单词分类";
        });
        listWordsExcludeChild(row.wordsId).then(response => {
          this.wordsOptions = this.handleTree(response.data, "wordsId");
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.wordsId != undefined) {
              updateWords(this.form).then(response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                }
              });
            } else {
              addWords(this.form).then(response => {
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
        this.$confirm('是否确认删除名称为"' + row.wordsName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
            console.log("删除勒？？？？？？？？？")
          return delWords(row.wordsId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      }
    }
  };
</script>

