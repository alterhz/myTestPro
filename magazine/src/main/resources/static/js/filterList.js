

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
        for (var i=0; i<myFields.length; ++i) {
            if (myFields[i].filterName == filterName) {
                myFields.splice(i, 1);
                console.debug("删除元素" + myFields[i]);
            }
        }
    }
  }
});

var myFields;

axios.get('/filters')
    .then(function(response) {
        console.log(response);
        myFields = response.data;

        // 处理filters.给每个filter添加addField字段
//        for (var key in myFields) {
//            console.log(myFields[key].sheetName);
//            myFields[key].addField = "empty";
//        }

        myFilter.filters = myFields;
    })
    .catch(function(error) {
        console.log(error);
    });