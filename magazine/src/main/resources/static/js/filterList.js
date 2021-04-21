

var myFilter = new Vue({
  el: '#filter',
  data: {
    filters: []
  },
  methods: {
    save: function(filter) {
        console.log(filter);
        axios({
            method:'post',
            url: '/filters',
            data: filter,
            headers: {'Content-Type': "application/json"},
        });
    },
    del: function(filterName) {
        console.log(filterName);
        axios({
            method:'delete',
            url: '/filters/' + filterName,
        });
    }
  }
});

var responseData;

axios.get('/filters')
    .then(function(response) {
        console.log(response);
        responseData = response.data;

        // 处理filters.给每个filter添加addField字段
        for (var key in responseData) {
            console.log(responseData[key].sheetName);
            responseData[key].addField = "empty";
        }

        myFilter.filters = responseData;
    })
    .catch(function(error) {
        console.log(error);
    });