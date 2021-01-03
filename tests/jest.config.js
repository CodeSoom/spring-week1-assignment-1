module.exports = {
  setupFilesAfterEnv: [
    'jest-plugin-context/setup',
  ],
  transform: {
    '^.+\\.[t|j]sx?$': 'babel-jest',
  },
};
