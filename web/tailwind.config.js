module.exports = {
  purge: ["./src/**/*.html", "./src/**/*.vue"],
  theme: {
    extend: {}
  },
  variants: {},
  plugins: [],
  future: {
    removeDeprecatedGapUtilities: true,
    purgeLayersByDefault: true,
    standardFontWeights: true,
    defaultLineHeights: true
  },
};
