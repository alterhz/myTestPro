

var myFilter = new Vue({
  el: '#filter',
  data: {
    filters: [],
    schema: [],
  },
  methods: {
    save: function(filter) {
        // 只允许有一个默认搜索键
//        let count = 0;
//        for (var key in filter.fields) {
//            if (filter.fields[key].defaultKey == true) {
//                ++count;
//            }
//            console.log("field.defaultKey = " + filter.fields[key]);
//        }
//        console.log("count = " + count);
//        if (count != 1) {
//            alert("默认搜索键有且必须只有一个。");
//            return;
//        }

        console.log(filter);
        axios({
            method:'post',
            url: '/filters',
            data: filter,
            headers: {'Content-Type': "application/json"},
        });
        sortMyFields();
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
    },
    selectDefaultKey: function(filter, key) {
        for (var key in filter.fields) {
            if (filter.fields[key].field == key) {
                filter.fields[key].defaultKey = true;
            } else {
                filter.fields[key].defaultKey = false;
            }
        }
    },
    selectSortKey: function(filter, key) {
            for (var key in filter.fields) {
                if (filter.fields[key].field == key) {
                    filter.fields[key].sortKey = true;
                } else {
                    filter.fields[key].sortKey = false;
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

        myFilter.filters = myFields;
        sortMyFields();
    })
    .catch(function(error) {
        console.log(error);
    });

function sortMyFields() {
    for (var key in myFields) {
        myFields[key].fields.sort((a, b) => { return a.order - b.order; });
    }

    getSchema();
}

function getSchema() {
    for (var key in myFields) {
        var filterName = myFields[key].filterName;
        console.log('filterName = ' + filterName);
        axios.get('/view/fields')
            .then(response => {
                console.log(response);
                myFilter.schema = response.data;
            })
            .catch(err => {
                console.log(err);
            });
    }

}