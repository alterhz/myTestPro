
/**
* 搜索
*/
var mySearch = new Vue({
    el: '#mySearch',
    data: {
        searchText: '',
        searchField: '',
        keys: {},   //检索字段的key
        showMore: false,    //是否显示多字段搜索
        fields: [], //字段列表
        orgRows: [],    //原始行数据
        rows: []   //检索后的数据
    },
    methods: {
        search: function() {
//            console.log(this.searchText);
            // concat复制行数数据
            var filterRows = this.orgRows.concat();
            if (this.showMore) {
                // 多字段检索
                this.rows = filterRows.filter(row => {
                    for (var key in this.keys) {
                        if (row[key].indexOf(this.keys[key]) === -1) {
                            return false;
                        }
                    }
                    return true;
                });



            } else {
                // 默认单字段检索
                console.log("单字段检索.this.searchText = " + this.searchText);
                this.rows = filterRows.filter(row => row[this.searchField].indexOf(this.searchText) !== -1);
            }


        }
    }
});

// 获取url参数
function getQueryString(name) {
  let reg = `(^|&)${name}=([^&]*)(&|$)`
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return null;
}

// 获取url参数中的过滤器名称
var filter = getQueryString("filter");
console.log(filter);
axios.get('/view?filter=' + filter)
    .then(function(response) {
        console.log(response);
        var myFields = response.data;

        var searchField = myFields.searchField;
        // 按照搜索键进行排序
        sortRows = myFields.rows.concat();
        if (searchField !== undefined && searchField.length > 0) {
            sortRows.sort(function(a, b) {
                // {sensitivity: 'base'}
                if (a[searchField] !== undefined && b[searchField] !== undefined) {
                    return a[searchField].localeCompare(b[searchField], 'zh-Hans-CN', {sensitivity: 'accent'});
                } else {
                    return true;
                }
            });

            console.debug(sortRows);
        }

        // 只需要做一次替换
        sortRows.forEach(row => {
           for (var key in row) {
               var url = row[key];
               if (url.indexOf('http') !== -1) {
                   row[key] = '<a href="' + url + '" target="_blank">' + url + '</a> <button class="btn badge" data-clipboard-text="' + url + '">复制</button>';
                   console.debug("replace" + url);
               }
           }
        });

        // 默认搜索字段
        mySearch.searchField = searchField;
        // 字段数据
        mySearch.fields = myFields.fields;
        // 原始数据，每次搜索都使用原始数据过滤
        mySearch.orgRows = sortRows;
        mySearch.rows = mySearch.orgRows.concat();
//        console.log(mySearch.rows);

        runClipboard();
    })
    .catch(function(error) {
        console.log(error);
    });

    function runClipboard() {
        var clipboard = new ClipboardJS('.btn');

        clipboard.on('success', function(e) {
            console.debug('Action:', e.action);
            console.debug('Text:', e.text);
            console.debug('Trigger:', e.trigger);

            e.clearSelection();

            alert("网址[" + e.text + "]已复制到剪切板");
        });

        clipboard.on('error', function(e) {
            console.error('Action:', e.action);
            console.error('Trigger:', e.trigger);
        });
    }