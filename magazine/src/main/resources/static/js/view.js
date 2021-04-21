
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
                var rs = filterRows.filter(row => row[myFields.searchField].indexOf(this.searchText) !== -1);
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
var myFields;
var sortRows;

function getQueryString(name) {
  let reg = `(^|&)${name}=([^&]*)(&|$)`
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return null;
}
var filter = getQueryString("filter");
console.log(filter);
axios.get('/view?filter=' + filter)
    .then(function(response) {
        console.log(response);
        myFields = response.data;

        mySheet.fields = myFields.fields;

        // 按照搜索键进行排序
        sortRows = myFields.rows.concat();
        if (myFields.searchField !== undefined && myFields.searchField.length > 0) {
            sortRows.sort(function(a, b) {
                // {sensitivity: 'base'}
                if (a[myFields.searchField] !== undefined && b[myFields.searchField] !== undefined) {
                    return a[myFields.searchField].localeCompare(b[myFields.searchField], 'zh-Hans-CN', {sensitivity: 'accent'});
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

        mySheet.rows = sortRows;

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