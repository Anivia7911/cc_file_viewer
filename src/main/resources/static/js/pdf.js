function downloadPDF() {
    const link = document.createElement('a');
    link.href = /*[(${fileAttribute.filePath})]*/ '#';
    link.download = /*[(${fileAttribute.fileName})]*/ 'document.pdf';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function printPDF() {
    const iframe = document.getElementById('pdfViewer');
    iframe.contentWindow.print();
}