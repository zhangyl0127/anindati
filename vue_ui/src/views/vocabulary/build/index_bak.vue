<template>
  <el-table
    :data="tableData"
    @row-click="clickTable"
    ref="refTable"
    style="width: 100%">
    <el-table-column type="expand">
      <template slot-scope="props">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item label="商品名称">
            <span>{{ props.row.name }}</span>
          </el-form-item>
          <el-form-item label="所属店铺">
            <span>{{ props.row.shop }}</span>
          </el-form-item>
          <el-form-item label="商品 ID">
            <span>{{ props.row.id }}</span>
          </el-form-item>
          <el-form-item label="店铺 ID">
            <span>{{ props.row.shopId }}</span>
          </el-form-item>
          <el-form-item label="商品分类">
            <span>{{ props.row.category }}</span>
          </el-form-item>
          <el-form-item label="店铺地址">
            <span>{{ props.row.address }}</span>
          </el-form-item>
          <el-form-item label="商品描述">
            <span>{{ props.row.desc }}</span>
          </el-form-item>
          <el-form-item label="修改描述啊">
            <span v-for="(value,key) in props.row.children">第{{ value.num}}次修改,修改内容为：{{ value.introduce}}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <el-table-column
      label="商品 ID"
      prop="id">
    </el-table-column>
    <el-table-column
      label="商品名称"
      prop="name">
    </el-table-column>
    <el-table-column
      label="描述"
      prop="desc"
      show-overflow-tooltip="true">
    </el-table-column>
  </el-table>
</template>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

<script>
  export default {
    data() {
      return {
        tableData: [{
          id: '12987122',
          name: '好滋好味鸡蛋仔',
          category: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻荷兰优质淡奶，奶香浓而不腻',
          address: '上海市普陀区真北路',
          shop: '王小虎夫妻店',
          shopId: '10333',
          children:[
            {
              num:"1",
              introduce:"很不错的东西",
              updateBy:"曾玉"
            },
            {
              num:"2",
              introduce:"这是我的修改啊啊啊啊啊",
              updateBy:"林眸"
            }
          ]
        }, {
          id: '12987123',
          name: '好滋好味鸡蛋仔',
          category: '江浙小吃、小吃零食',
          desc: '荷兰优质淡奶，奶香浓而不腻',
          address: '上海市普陀区真北路',
          shop: '王小虎夫妻店',
          shopId: '10333'
        }]
      }
    },
    methods: {
      clickTable(row,index,e){
        this.$refs.refTable.toggleRowExpansion(row)
      }
    }
  }
</script>
