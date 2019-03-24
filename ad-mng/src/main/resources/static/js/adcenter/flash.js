$(function(){
    var FileUrlHelper = {
        mainFileUrl:"",
        extraFileUrls:[],
        /////
        setFileUrlInput:function(){
            var allFileUrls = [];
            allFileUrls.push(this.mainFileUrl);
            $.each(this.extraFileUrls, function(index, element){
                allFileUrls.push(element);
            });
            var $fileUrl = $("[name='fileUrl']");
            $fileUrl.val(allFileUrls.join(";"));
            $fileUrl.attr("title", allFileUrls.join("\n"));
        },
        setFileSizeInput:function(fileSize) {
            $("[name='fileSize']").val(fileSize);
        },
        setMainFileUrl:function(location) {
            if (location) {
                this.mainFileUrl = location;
                this.setFileUrlInput();
            }
        },
        addExtraFileUrl:function(location) {
            if (!this.mainFileUrl) {
                alert("请确保入口SWF文件已上传成功");
                return;
            }
            if (location && !ArrayUtil.contains(this.extraFileUrls, location)) {
                this.extraFileUrls.push(location);
                this.setFileUrlInput();
            }
        }
    };
    var fileMap = new Object();
    function sumFileSize(){
        var sum = 0;
        for(var file in fileMap){
            var size = fileMap[file];
            sum += size;
        }
        return sum;
    }
    FileUploadUtil.bind("#mainFileUploadBtn", "/cdn/save.do", "file", function(file, result){
        fileMap[result.message] = file.size;
        console.log("main file", file);
        console.log("main result", result);
        FileUrlHelper.setMainFileUrl(result.message);
        var size = sumFileSize();
        FileUrlHelper.setFileSizeInput(size);
    });
    FileUploadUtil.bind("#extraFileUploadBtn", "/cdn/save.do", "file", function(file, result){
        fileMap[result.message] = file.size;
        console.log("extra file", file);
        console.log("extra result", result);
        FileUrlHelper.addExtraFileUrl(result.message);
        var size = sumFileSize();
        FileUrlHelper.setFileSizeInput(size);
    });
});