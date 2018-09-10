<template>
        <el-form style="margin:20px;width:60%;min-width:100%"
                label-width="150px">
                <el-button
                        type="primary"
                        @click='innerTags()'>内置标签
                </el-button>
                <el-button
                        type="primary"
                        @click='skillTags()'>技能标签
                </el-button>
                <el-button
                        type="primary"
                        @click='interestTags()'>兴趣标签
                </el-button>
                <el-button
                        type="primary"
                        @click='productTags()'>商品标签
                </el-button>
        </el-form>
</template>
<script>
  import TagsJs from './tags.js'

  export default TagsJs
</script>
<style scoped lang='less'>

</style>
