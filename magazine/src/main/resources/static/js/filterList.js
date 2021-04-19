

var myFilter = new Vue({
  el: '#filter',
  data: {
    filters: []
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