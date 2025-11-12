export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: theme => ({
        'background-img': "url('/assets/backgroundImg.png')",
      }),
      fontFamily: {
        sans: ['Poppins', 'ui-sans-serif', 'system-ui'],
      },
    },
  },  
  plugins: [],
}
