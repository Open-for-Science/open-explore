module.exports = {
  themeColor: '#2F3A91',
  getThemeColors: function (color, series, varyColor, others) {
    let colors = series(color)
    colors.push(
      varyColor.mix('#000', color, 0.1),
      varyColor.mix('#000', color, 0.2),
      varyColor.mix('#000', color, 0.3),
      varyColor.mix('#000', color, 0.4),
      varyColor.mix('#000', color, 0.5),
      varyColor.mix('#000', color, 0.6),
      varyColor.mix('#000', color, 0.7),
      varyColor.mix('#000', color, 0.8),
      varyColor.mix('#000', color, 0.9)
    );
    colors.push.apply(colors, others)
    return colors
  }
}
