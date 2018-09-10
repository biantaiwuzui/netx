export default {
  name: 'form',
  data () {
    return {
      form: {
        name: '',
        region: '',
        region2: [],
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: '',
        disk_size: 50
      }
    }
  },
  methods: {
    onSubmit () {
      alert(11)
      console.log('submit!')
    },
    handleRemove (file) {},
    handlePreview (file) {
      console.log(file)
    }
  }
}
