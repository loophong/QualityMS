<template>
    <el-dialog title="选择审批人" :visible.sync="dialogVisible" width="50%">
        <el-form :model="form">
            <el-form-item label="审批人" :label-width="formLabelWidth">
                <el-select v-model="form.approver" placeholder="请选择审批人">
                    <el-option v-for="item in approvers" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submitApprover">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
export default {
    data() {
        return {
            dialogVisible: false,
            form: {
                approver: ''
            },
            formLabelWidth: '120px',
            approvers: [] // 假设这是从后端获取的审批人列表
        };
    },
    methods: {
        submitApprover() {
            // 提交审批人信息到后端
            this.dialogVisible = false;
        }
    }
};
</script>