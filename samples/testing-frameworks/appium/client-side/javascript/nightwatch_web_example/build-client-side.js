const fs = require('fs');
const path = require('path');

function buildClientSide() {
  const credentials = require('./.credentials.json');
  const apiKey = credentials["apiKey"];
  const nightwatchConfigPath = path.join(__dirname, 'nightwatch.json');
  const buildPath = path.join(__dirname, 'build', 'nightwatch.json');

  let config = fs.readFileSync(nightwatchConfigPath, 'utf8');
  config = config.replaceAll("<insert_your_apiKey_to_credentials_file>", apiKey);

  fs.mkdirSync(path.dirname(buildPath), { recursive: true });
  fs.writeFileSync(buildPath, config, 'utf8');
}

buildClientSide();
