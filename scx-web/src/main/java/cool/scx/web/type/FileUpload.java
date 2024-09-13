package cool.scx.web.type;

public interface FileUpload {

  /**
   * @return the name of the upload as provided in the form submission
   */
  String name();

  /**
   * @return the actual temporary file name on the server where the file was uploaded to.
   */
  String uploadedFileName();

  /**
   * @return the file name of the upload as provided in the form submission
   */
  String fileName();

  /**
   * @return the size of the upload, in bytes
   */
  long size();

  /**
   * @return the content type (MIME type) of the upload
   */
  String contentType();

  /**
   * @return the content transfer encoding of the upload - this describes how the upload was encoded in the form submission.
   */
  String contentTransferEncoding();

  /**
   * @return the charset of the upload
   */
  String charSet();

  /**
   * Try to cancel the file upload.
   *
   * @return {@code true} when the upload was cancelled, {@code false} when the upload is finished and the file is available
   */
  boolean cancel();

}
