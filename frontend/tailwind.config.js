/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/**/*.html',
    './src/**/*.ts',
  ],
  theme: {
    extend: {
      colors: {
        'primary': '#0056b3',
        'secondary': '#0096c7',
        'accent': '#ff6b6b',
        'background': '#f8f9fa',
        'text': '#343a40',
      }
    }
  },
  plugins: [],
}

