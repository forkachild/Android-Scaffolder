const fs = require('fs');
const path = require('path');

const NAME = 'NAME';
const PACKAGE = 'PACKAGE';
const TEMPLATE = 'TEMPLATE';
const OUTPUT = 'OUTPUT';

const PATTERN_NAME = '{{NAME}}';
const PATTERN_DOMAIN = '{{PACKAGE}}';

const args = (() => {
    let obj = {};

    process.argv.forEach(item => {
        if (item.indexOf('=') != -1) {
            const tokens = item.split('=');
            obj[tokens[0]] = tokens[1];
        }
    })

    if (!obj[NAME] || !obj[PACKAGE] || !obj[TEMPLATE]) {
        throw new Error('Please provide NAME, PACKAGE, TEMPLATE and optinally OUTPUT in Key=Value format')
    }

    if (!obj[OUTPUT]) {
        obj[OUTPUT] = '.';
    }

    return obj;
})();

String.prototype.replaceAll = function (search, replacement) {
    return this.replace(new RegExp(search.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1"), 'g'), replacement);
};

const name = args[NAME];
const package = args[PACKAGE];
const packagePath = package.replaceAll('.', path.sep);
const template = args[TEMPLATE];
const output = args[OUTPUT];

const normalizePath = (input) => path.join(output, input.substring((template + path.sep).length));
const replaceInText = (input) => input.replaceAll(PATTERN_NAME, name).replaceAll(PATTERN_DOMAIN, package);
const replaceInPath = (input) => input.replaceAll(PATTERN_NAME, name).replaceAll(PATTERN_DOMAIN, packagePath);

const getAllFiles = (dir, filelist) => {
    if (!dir) {
        dir = path.join('./', template);
    }

    if (!filelist) {
        filelist = [];
    }

    const files = fs.readdirSync(dir);

    files.forEach(file => {

        const stat = fs.statSync(path.join(dir, file));

        if (stat.isDirectory()) {
            filelist = getAllFiles(path.join(dir, file), filelist);
        } else {
            filelist.push({
                originalDir: dir,
                originalName: file,
                newDir: replaceInPath(normalizePath(dir)),
                newName: replaceInPath(file)
            });
        }

    });

    return filelist;
};

const getUniqueDirectories = (files) => {
    const dirs = new Set();
    files.forEach(file => dirs.add(file.newDir));
    return dirs;
}

const files = getAllFiles();
const dirs = getUniqueDirectories(files);

dirs.forEach(dir => fs.mkdirSync(dir, { recursive: true }));
files.forEach(file => {
    const originalContent = fs.readFileSync(path.join(file.originalDir, file.originalName), { encoding: 'utf-8' });
    const resolvedContent = replaceInText(originalContent);
    fs.writeFileSync(path.join(file.newDir, file.newName), resolvedContent, { encoding: 'utf-8', flag: 'wx' });
});