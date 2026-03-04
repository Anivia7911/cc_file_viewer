document.addEventListener('DOMContentLoaded', function() {
    const printBtn = document.getElementById('printBtn');
    const pdfViewer = document.getElementById('pdfViewer');

    if (printBtn && pdfViewer) {
        printBtn.addEventListener('click', function() {
            // 触发 iframe 内部的打印功能
            pdfViewer.contentWindow.print();
        });
    }
    // 下载功能已移至 HTML 的 <a> 标签处理，更加稳健
});