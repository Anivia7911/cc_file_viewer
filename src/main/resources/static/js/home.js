document.addEventListener('DOMContentLoaded', function() {
    const browseBtn = document.getElementById('browseBtn');
    const previewBtn = document.getElementById('previewBtn');
    const clearBtn = document.getElementById('clearBtn');
    const fileInput = document.getElementById('fileInput');
    const fileList = document.getElementById('fileList');
    const fileUrl = document.getElementById('fileUrl');
    const previewUrlBtn = document.getElementById('previewUrlBtn');
    const previewPaneContent = document.getElementById('preview-pane-content');
    
    // 选项卡切换
    const tabs = document.querySelectorAll('.tab-button');
    const panes = document.querySelectorAll('.content-pane');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // 移除所有活动标签和面板
            tabs.forEach(t => t.classList.remove('active'));
            panes.forEach(p => p.classList.remove('active'));

            // 激活当前标签和面板
            tab.classList.add('active');
            const targetPane = document.getElementById(tab.dataset.tab + '-pane');
            if (targetPane) {
                targetPane.classList.add('active');
            }
        });
    });
    
    // 监听URL输入框的变化
    if (fileUrl) {
        fileUrl.addEventListener('input', function() {
            if (this.value.trim() !== '') {
                previewUrlBtn.disabled = false;
            } else {
                previewUrlBtn.disabled = true;
            }
        });
    }
    
    // URL预览按钮点击事件
    if (previewUrlBtn) {
        previewUrlBtn.addEventListener('click', function() {
            if (fileUrl.value.trim() !== '') {
                // 切换到预览面板
                switchToPreviewPane();
                
                // 显示加载中提示
                previewPaneContent.innerHTML = '<div style="display: flex; justify-content: center; align-items: center; flex: 1; min-height: 75vh;">加载中...</div>';
                
                // 构造预览URL并嵌入iframe
                const encodedUrl = btoa(encodeURIComponent(fileUrl.value.trim()));
                const previewUrl = '/preview?filePath=' + encodeURIComponent(encodedUrl);
                
                // 在预览区域嵌入iframe
                previewPaneContent.innerHTML = `<iframe src="${previewUrl}" style="flex: 1; width: 100%; border: none; min-height: 75vh;"></iframe>`;
            }
        });
    }
    
    let selectedFiles = [];
    
    // 点击选择文件按钮时触发文件选择
    browseBtn.addEventListener('click', function() {
        fileInput.click();
    });
    
    // 文件选择改变时的处理
    fileInput.addEventListener('change', function(e) {
        // 只有当选择了文件时才更新文件列表
        if (e.target.files.length > 0) {
            selectedFiles = Array.from(e.target.files);
            updateFileList();
            previewBtn.style.display = 'inline-block';
            clearBtn.style.display = 'inline-block';
        }
        // 如果没有选择文件，则保留原有的文件列表和预览按钮状态
    });
    
    // 点击预览按钮时的处理
    previewBtn.addEventListener('click', function() {
        if (selectedFiles.length > 0) {
            // 创建一个FormData对象用于上传文件
            const formData = new FormData();
            formData.append('file', selectedFiles[0]);
            
            // 显示加载中提示
            previewPaneContent.innerHTML = '<div style="display: flex; justify-content: center; align-items: center; flex: 1; min-height: 75vh;">上传并加载中...</div>';
            
            // 先上传文件
            fetch('/upload', {
                method: 'POST',
                body: formData
            })
            .then(response => response.text())
            .then(filePath => {
                const previewUrl = '/preview?filePath=' + encodeURIComponent(filePath);
                
                // 切换到预览面板
                switchToPreviewPane();
                
                // 在预览区域嵌入iframe
                previewPaneContent.innerHTML = `<iframe src="${previewUrl}" style="flex: 1; width: 100%; border: none; min-height: 75vh;"></iframe>`;
            })
            .catch(error => {
                console.error('文件上传失败:', error);
                previewPaneContent.innerHTML = '<div style="display: flex; justify-content: center; align-items: center; flex: 1; min-height: 75vh; color: red;">文件上传失败</div>';
            });
        }
    });
    
    // 点击清空按钮时的处理
    clearBtn.addEventListener('click', function() {
        // 清空文件列表
        selectedFiles = [];
        
        // 更新文件列表显示
        updateFileList();
        
        // 隐藏预览和清空按钮
        previewBtn.style.display = 'none';
        clearBtn.style.display = 'none';
        
        // 清空文件输入框
        fileInput.value = '';
    });
    
    // 更新文件列表显示
    function updateFileList() {
        fileList.innerHTML = '';
        
        if (selectedFiles.length === 0) {
            return;
        }
        
        const title = document.createElement('h4');
        title.textContent = '已选择的文件:';
        fileList.appendChild(title);
        
        selectedFiles.forEach((file, index) => {
            const fileItem = document.createElement('div');
            fileItem.className = 'file-item';
            
            // 创建文件项内容容器
            const fileItemContent = document.createElement('div');
            fileItemContent.className = 'file-item-content';
            
            // 创建文件信息容器
            const fileInfo = document.createElement('div');
            fileInfo.className = 'file-info';
            
            const fileName = document.createElement('span');
            fileName.className = 'file-name ' + getFileTypeClass(file.name);
            fileName.textContent = file.name;
            
            const fileType = document.createElement('span');
            fileType.className = 'file-type';
            fileType.textContent = getFileType(file.name);
            
            const fileSize = document.createElement('span');
            fileSize.className = 'file-size';
            fileSize.textContent = formatFileSize(file.size);
            
            fileInfo.appendChild(fileName);
            fileInfo.appendChild(fileType);
            fileInfo.appendChild(fileSize);
            
            fileItemContent.appendChild(fileInfo);
            fileItem.appendChild(fileItemContent);
            fileList.appendChild(fileItem);
        });
    }
    
    // 格式化文件大小
    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }
    
    // 根据文件扩展名获取文件类型
    function getFileType(fileName) {
        const extension = fileName.split('.').pop().toLowerCase();
        const fileTypes = {
            'jpg': '图片', 'jpeg': '图片', 'png': '图片', 'gif': '图片', 'bmp': '图片', 
            'webp': '图片', 'svg': '图片', 'ico': '图片',
            'mp4': '视频', 'avi': '视频', 'mov': '视频', 'wmv': '视频', 'flv': '视频', 
            'mkv': '视频', 'webm': '视频',
            'mp3': '音频', 'wav': '音频', 'ogg': '音频', 'flac': '音频', 'aac': '音频', 'wma': '音频',
            'zip': '压缩包', 'rar': '压缩包', '7z': '压缩包', 'tar': '压缩包', 'gz': '压缩包', 'bz2': '压缩包',
            'pdf': 'PDF文档', 'doc': 'Word文档', 'docx': 'Word文档', 'txt': '文本文档', 
            'rtf': '文本文档', 'md': '文本文档', 'odt': '文本文档',
            'js': '代码文件', 'html': '代码文件', 'css': '代码文件', 'java': '代码文件', 
            'py': '代码文件', 'php': '代码文件', 'cpp': '代码文件', 'c': '代码文件'
        };
        
        return fileTypes[extension] || '未知类型';
    }
    
    // 根据文件扩展名获取文件类型CSS类
    function getFileTypeClass(fileName) {
        const extension = fileName.split('.').pop().toLowerCase();
        
        // 图片文件
        const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico'];
        if (imageExtensions.includes(extension)) {
            return 'image';
        }
        
        // 视频文件
        const videoExtensions = ['mp4', 'avi', 'mov', 'wmv', 'flv', 'mkv', 'webm'];
        if (videoExtensions.includes(extension)) {
            return 'video';
        }
        
        // 音频文件
        const audioExtensions = ['mp3', 'wav', 'ogg', 'flac', 'aac', 'wma'];
        if (audioExtensions.includes(extension)) {
            return 'audio';
        }
        
        // 压缩文件
        const archiveExtensions = ['zip', 'rar', '7z', 'tar', 'gz', 'bz2'];
        if (archiveExtensions.includes(extension)) {
            return 'archive';
        }
        
        // 文档文件
        const documentExtensions = ['pdf', 'doc', 'docx', 'txt', 'rtf', 'md', 'odt'];
        if (documentExtensions.includes(extension)) {
            return 'document';
        }
        
        // 代码文件
        const codeExtensions = ['js', 'html', 'css', 'java', 'py', 'php', 'cpp', 'c', 'h', 'cs', 'go', 'rb', 'swift'];
        if (codeExtensions.includes(extension)) {
            return 'code';
        }
        
        // 默认文件类型
        return '';
    }
    
    // 删除指定索引的文件（保留此函数以防将来需要）
    function deleteFile(index) {
        // 从selectedFiles数组中移除指定文件
        selectedFiles.splice(index, 1);
        
        // 更新文件列表显示
        updateFileList();
        
        // 如果没有文件了，隐藏预览按钮和清空按钮
        if (selectedFiles.length === 0) {
            previewBtn.style.display = 'none';
            clearBtn.style.display = 'none';
        }
        
        // 更新文件输入框的值
        const fileInput = document.getElementById('fileInput');
        if (fileInput) {
            // 创建一个新的FileList对象（由于FileList是只读的，我们需要用DataTransfer来模拟）
            const dataTransfer = new DataTransfer();
            selectedFiles.forEach(file => {
                dataTransfer.items.add(file);
            });
            fileInput.files = dataTransfer.files;
        }
    }
    
    // 切换到预览面板
    function switchToPreviewPane() {
        // 隐藏explanation-pane
        const explanationPane = document.getElementById('explanation-pane');
        if (explanationPane) {
            explanationPane.style.display = 'none';
        }
        
        // 显示preview-pane
        const previewPane = document.getElementById('preview-pane');
        if (previewPane) {
            previewPane.style.display = 'block';
        }
        
        // 注意：这里不再移除标签和面板的active类，以保持当前选中的标签状态
        // 这样用户上传后仍然可以看到他们之前选择的是哪个标签（本地上传还是链接上传）
    }
});