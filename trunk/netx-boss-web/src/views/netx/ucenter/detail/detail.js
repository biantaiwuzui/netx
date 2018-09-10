// import { gbs } from 'config/settings.js'

export default {
  name: 'user-detail',
  data () {
    return {
      userProfile: {
      },
      nickname :""
    }
  },
  methods: {
      fetchData(){
          if(this.$route.query.userId != null){
              var data = {userId : this.$route.query.userId}
              this.nickname = this.$route.query.nickname
              this.$$api_user_getUserProfile({
                  data,
                  fn: result => {
                      this.userProfile = result.userProfile
                  }
              })
          }
      },
      onSubmit(){
          var data={
              userId:this.userProfile.userId,
              nickname:this.nickname,
              homeTown:this.userProfile.homeTown,
              oftenIn:this.userProfile.oftenIn,
              alreadyTo:this.userProfile.alreadyTo,
              wantTo:this.userProfile.wantTo,
              introduce:this.userProfile.introduce,
              disposition:this.userProfile.disposition,
              appearance:this.userProfile.appearance,
              income:this.userProfile.income,
              maxIncome:this.userProfile.maxIncome,
              emotion:this.userProfile.emotion,
              height:this.userProfile.height,
              weight:this.userProfile.weight,
              nation:this.userProfile.nation,
              animalSigns:this.userProfile.animalSigns,
              starSign:this.userProfile.starSign,
              bloodType:this.userProfile.bloodType,
              address:this.userProfile.address,
              updateUserId: JSON.parse(window.localStorage.netx_userinfo).username,
          }
          var comfirm = confirm("确定修改吗?")
          if(comfirm) {
              this.$$api_user_updateUserDetails({
                  data,
                  fn: () => {
                      this.$message('操作成功')
                  }
              })
          }else{
              this.$message('操作取消')
          }
      },
      selectUserDetail(userId,nickname) {
          this.dialog.show = false
          this.$router.push({
              path : "/netx/ucenter/detail/",
              query:{
                  userId: userId,
                  nickname: nickname
              }
          })
      },


  },
    watch : {
        '$route' : 'fetchData'
    },
  created () {
  },
  mounted () {
      this.fetchData()
  },
    '$route' (to, from) {
    }
}
