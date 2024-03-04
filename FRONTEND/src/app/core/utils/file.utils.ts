
class FileUtils {

  getImageUrl(image: string): string {
    return 'data:image/jpeg;base64,' + image;
  }
}

export const fileUtils = new FileUtils();
