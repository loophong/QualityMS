<!-- DepartmentSearch.vue -->
<template>
    <div v-if="visible" class="department-search">
      <el-select v-model="selectedDepartment" placeholder="请选择科室">
        <el-option
          v-for="department in departments"
          :key="department.id"
          :label="department.name"
          :value="department.id"
        ></el-option>
      </el-select>
      <el-table :data="employees" style="width: 100%">
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="title" label="职称"></el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  export default {
    props: ['visible'],
    data() {
      return {
        selectedDepartment: '',
        departments: [], // 科室数据
        employees: [] // 员工数据
      };
    },
    watch: {
      selectedDepartment(val) {
        if (val) {
          this.fetchEmployeesByDepartment(val);
        }
      }
    },
    methods: {
      fetchDepartments() {
        // 加载科室数据
      },
      fetchEmployeesByDepartment(departmentId) {
        // 加载指定科室的员工数据
      }
    },
    created() {
      this.fetchDepartments();
    }
  };
  </script>