// import { gbs } from 'config/settings.js'

export default {
  name: 'system-tags',
  data () {
    return {
    }
  },
  methods: {

    innerTags(){
        this.$router.push({
            path : "/netx/system/innerTags/"
        })
    },
    skillTags(){
        this.$router.push({
            path : "/netx/system/skillTags/"
        })
    },
    interestTags(){
        this.$router.push({
            path : "/netx/system/interestTags/"
        })
    },
    productTags(){
        this.$router.push({
            path : "/netx/system/productTags/"
        })
    }
   
  },
  created () {
  },
  mounted () {
  }
}
