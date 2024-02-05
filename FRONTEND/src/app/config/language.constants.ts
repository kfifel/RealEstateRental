/*
    Languages codes are ISO_639-1 codes, see http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    They are written in English to avoid character encoding issues (not a perfect solution)
*/
export const LANGUAGES: string[] = [
  'en',
  'ar-ma',
  'fr',
  // jhipster-needle-i18n-language-constant - JHipster will add/remove languages in this array
];

export const MENU_LANGUAGES = [
  { text: 'العربية', flag: 'assets/images/flags/ma.jpg', lang: 'ar-ma' },
  { text: 'English', flag: 'assets/images/flags/us.jpg', lang: 'en' },
  { text: 'Français', flag: 'assets/images/flags/french.jpg', lang: 'fr' },
];
