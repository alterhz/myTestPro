
var mySearch = new Vue({
    el: '#mySearch',
    data: {
        searchText: ''
    },
    methods: {
        search: function() {
            console.log(this.searchText);
            var filterRows = sortRows.concat();
            if (this.searchText.length === 0) {
                mySheet.rows = filterRows;
                console.log('show directly.');
            } else {
                var rs = filterRows.filter(row => row[responseData.searchField].indexOf(this.searchText) !== -1);
                mySheet.rows = rs;
            }
        }
    }
});

var mySheet = new Vue({
    el: '#sheet',
    data: {
        rows: [],
        fields: []
    }
});

// 原始数据
var responseData;
var sortRows;

axios.get('/view?filter=bookFilter')
    .then(function(response) {
        console.log(response);
        console.log(response.data);
        responseData = response.data;

        mySheet.fields = responseData.fields;

        console.log('responseData.searchField = ' + responseData.searchField);
        console.log(responseData.rows);
        // 按照搜索键进行排序
        sortRows = responseData.rows.concat();
        if (responseData.searchField !== undefined && responseData.searchField.length > 0) {
            sortRows.sort(function(a, b) {
                // {sensitivity: 'base'}
                return a[responseData.searchField].localeCompare(b[responseData.searchField], 'zh-Hans-CN', {sensitivity: 'accent'});
            });

            console.log(sortRows);
        }

        // 只需要做一次替换
        sortRows.forEach(row => {
           for (var key in row) {
               var url = row[key];
               console.log(url);
               if (url.indexOf('http') !== -1) {
                   row[key] = '<a href="' + url + '" target="_blank">' + url + '</a>';
                   console.log("replace" + url);
               }
           }
        });

        mySheet.rows = sortRows;
    })
    .catch(function(error) {
        console.log(error);
    });