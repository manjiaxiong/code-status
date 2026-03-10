const puppeteer = require('puppeteer');
const path = require('path');
const os = require('os');

(async () => {
    const browser = await puppeteer.launch({
        headless: 'new',
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    const page = await browser.newPage();

    // 设置视口大小
    await page.setViewport({ width: 1200, height: 800 });

    // 读取HTML文件
    const htmlPath = path.join(__dirname, 'study-plan-28days.html');
    await page.goto(`file://${htmlPath}`, {
        waitUntil: 'networkidle0'
    });

    // 获取桌面路径
    const desktopPath = path.join(os.homedir(), 'Desktop');
    const pdfPath = path.join(desktopPath, '28天Java全栈速成计划.pdf');

    // 生成PDF
    await page.pdf({
        path: pdfPath,
        format: 'A4',
        printBackground: true,
        margin: {
            top: '20px',
            bottom: '20px',
            left: '20px',
            right: '20px'
        }
    });

    console.log(`PDF已生成: ${pdfPath}`);

    await browser.close();
})();
