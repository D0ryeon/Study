
// ******************************************** //
// ******* ↓↓↓↓↓ 사용자 설정 영역 ↓↓↓↓↓ ******* //
// ******************************************** //

const EDITION_SIZE = 100;
const LAYERS_ORDER = [
  { name: "1.Drink" },
  { name: "2.Plate" },
  { name: "3.Bun" },
  { name: "4.Patty" },
  { name: "5.Extra" },
  { name: "6.Vegetable" },
  { name: "7.Base Patty" },
  { name: "8.Flag" },
  { name: "9.Side" },
];

const NAME_PREFIX = "10-DUCK";  
const DESCRIPTION = "당신은 쉽덕입니다.";
const BASE_URI = "ipfs://NewUriToReplace";

const BACKGROUND = {
  generate: true,
  brightness: "80%",
  static: false,
  default: "#000000",
};

// ******************************************** //
// ******* ↑↑↑↑↑ 사용자 설정 영역 ⬆⬆⬆⬆⬆ ******* //
// ******************************************** //

const basePath = process.cwd();
const { MODE } = require(`${basePath}/constants/blend_mode.js`);
const { NETWORK } = require(`${basePath}/constants/network.js`);

const network = NETWORK.eth;

const solanaMetadata = {
  symbol: "YC",
  seller_fee_basis_points: 1000, // Define how much % you want from secondary market sales 1000 = 10%
  external_url: "https://www.youtube.com/c/hashlipsnft",
  creators: [
    {
      address: "7fXNuer5sbZtaTEPhtJ5g5gNtuyRoKkvxdjEjEnPN4mC",
      share: 100,
    },
  ],
};

// If you have selected Solana then the collection starts from 0 automatically
const layerConfigurations = [
  {
    growEditionSizeTo: EDITION_SIZE,
    layersOrder: LAYERS_ORDER,
  },
];

const shuffleLayerConfigurations = false;

const debugLogs = false;

const format = {
  width: 512,
  height: 512,
  smoothing: false,
};

const gif = {
  export: false,
  repeat: 0,
  quality: 100,
  delay: 500,
};

const text = {
  only: false,
  color: "#ffffff",
  size: 20,
  xGap: 40,
  yGap: 40,
  align: "left",
  baseline: "top",
  weight: "regular",
  family: "Courier",
  spacer: " => ",
};

const pixelFormat = {
  ratio: 2 / 128,
};

const extraMetadata = {};

const rarityDelimiter = "#";

const uniqueDnaTorrance = 10000;

const preview = {
  thumbPerRow: 5,
  thumbWidth: 50,
  imageRatio: format.height / format.width,
  imageName: "preview.png",
};

const preview_gif = {
  numberOfImages: 5,
  order: "ASC", // ASC, DESC, MIXED
  repeat: 0,
  quality: 100,
  delay: 500,
  imageName: "preview.gif",
};

module.exports = {
  format,
  baseUri: BASE_URI,
  description: DESCRIPTION,
  background: BACKGROUND,
  uniqueDnaTorrance,
  layerConfigurations,
  rarityDelimiter,
  preview,
  shuffleLayerConfigurations,
  debugLogs,
  extraMetadata,
  pixelFormat,
  text,
  namePrefix: NAME_PREFIX,
  network,
  solanaMetadata,
  gif,
  preview_gif,
};
