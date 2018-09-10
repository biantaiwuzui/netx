export default {
    components: {
      // FormData
    },
    data () {
        return {
            list:[
                {
                    sendTime1: "",
                    sendCount1: "",
                    sendPeople1: ""
                },
                {
                    sendTime2: "",
                    sendCount2: "",
                    sendPeople2: ""
                },
                {
                    sendTime3: "",
                    sendCount3: "",
                    sendPeople3: ""
                },
                {
                    sendTime4: "",
                    sendCount4: "",
                    sendPeople4: ""
                },
                {
                    sendTime5: "",
                    sendCount5: "",
                    sendPeople5: ""
                }, 
            ],
            
        }
      },
      methods: {
          onSubmit () {
            var confim = confirm("你确定提交吗？")
            if(confim){
                var data = {
                    list: [
                        {
                          sendCount: this.list.sendCount1,
                          sendPeople: this.list.sendPeople1,
                          sendTime: this.list.sendTime1+":00"
                        },
                        {
                            sendCount: this.list.sendCount2,
                            sendPeople: this.list.sendPeople2,
                            sendTime: this.list.sendTime2+":00"
                        },
                        {
                            sendCount: this.list.sendCount3,
                            sendPeople: this.list.sendPeople3,
                            sendTime: this.list.sendTime3+":00"
                        },
                        {
                            sendCount: this.list.sendCount4,
                            sendPeople: this.list.sendPeople4,
                            sendTime: this.list.sendTime4+":00"
                        },
                        {
                            sendCount: this.list.sendCount5,
                            sendPeople: this.list.sendPeople5,
                            sendTime: this.list.sendTime5+":00"
                        }
                      ],
                      type: 1
                }
                this.$$api_system_add({
                    data,
                    fn: detFlag => {
                        this.$message.success("操作成功")
                    },
                    errFn:(data) => {
                        this.$message.error("操作失败")
                    }
                })
            }
          }
      },
  }
  