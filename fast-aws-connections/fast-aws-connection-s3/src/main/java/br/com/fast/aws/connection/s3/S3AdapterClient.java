package br.com.fast.aws.connection.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.com.fast.aws.connection.commons.utils.DateFormatter;

/**
 * @author Wagner.Alves
 */
public class S3AdapterClient {

	private AmazonS3 client;

	/**
	 * S3AdapterClient constructor
	 * 
	 * @param client
	 */
	public S3AdapterClient(AmazonS3 client) {
		this.client = client;
	}

	/**
	 * Creates a new Amazon S3 bucket with the specified name in the region that the
	 * client was created in. If no region or AWS S3 endpoint was specified when
	 * creating the client, the bucket will be created within the default (US)
	 * region, Region.US_Standard. Every object stored in Amazon S3 is contained
	 * within a bucket. Buckets partition the namespace of objects stored in Amazon
	 * S3 at the top level. Within a bucket, any name can be used for objects.
	 * However, bucket names must be unique across all of Amazon S3.
	 * 
	 * @param bucketName
	 * @return Bucket
	 */
	public Bucket criaBucket(String bucketName) {
		return client.createBucket(bucketName.toLowerCase());
	}

	/**
	 * Deletes the specified bucket. All objects (and all object versions, if
	 * versioning was ever enabled) in the bucket must be deleted before the bucket
	 * itself can be deleted. Only the owner of a bucket can delete it, regardless
	 * of the bucket's access control policy (ACL).
	 * 
	 * @param bucketName
	 */
	public void deletaBucket(String bucketName) {
		DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucketName);
		client.deleteBucket(deleteBucketRequest);

	}

	/**
	 * Clean all the objects that are stored in the specified bucked
	 * 
	 * @param bucketName
	 */
	public void esvaziaBucket(String bucketName) {
		ObjectListing objects = client.listObjects(bucketName);
		for (Iterator<?> iterator = objects.getObjectSummaries().iterator(); iterator.hasNext();) {
			S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
			client.deleteObject(bucketName, summary.getKey());
		}
	}

	/**
	 * Returns a list of all Amazon S3 buckets that the authenticated sender of the
	 * request owns. Users must authenticate with a valid AWS Access Key ID that is
	 * registered with Amazon S3. Anonymous requests cannot list buckets, and users
	 * cannot list buckets that they did not create.
	 * 
	 * @return List<Bucket>
	 */
	public List<Bucket> listarBuckets() {
		return client.listBuckets();
	}

	/**
	 * Returns a list of bucket names.
	 * 
	 * @return List<String>
	 */
	public List<String> listarBucketNames() {
		List<String> bucketNames = new ArrayList<>();
		for (Bucket b : client.listBuckets()) {
			bucketNames.add(b.getName());
		}
		return bucketNames;
	}

	/**
	 * Uploads a new object to the specified Amazon S3 bucket. The PutObjectRequest
	 * contains all the details of the request, including the bucket to upload to,
	 * the key the object will be uploaded under, and the file or input stream
	 * containing the data to upload.
	 * 
	 * @param bucketName
	 * @param folderName
	 * @return
	 */
	public PutObjectResult createFolder(String bucketName, String folderName) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, emptyContent, metadata);
		// send request to S3 to create folder
		return client.putObject(putObjectRequest);
	}

	/**
	 * Remove a folder in the specified bucket Deletes the specified object in the
	 * specified bucket. Once deleted, the object can only be restored if versioning
	 * was enabled when the object was deleted. If attempting to delete an object
	 * that does not exist, Amazon S3 returns a success message instead of an error
	 * message.
	 * 
	 * @param bucketName
	 * @param folderName
	 */
	public void removeFolder(String bucketName, String folderName) {
		List<S3ObjectSummary> objectSummaries = client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : objectSummaries) {
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
	}

	/**
	 * Upload a object to the specified bucket, Uploads the specified input stream
	 * and object metadata to Amazon S3 under the specified bucket and key name.
	 * Amazon S3 never stores partial objects; if during this call an exception
	 * wasn't thrown, the entire object was stored.
	 * 
	 * @param bucketName
	 * @param conteudo
	 * @param nomeArquivo
	 * @return PutObjectResult
	 */
	public PutObjectResult enviaParaBucket(String bucketName, String conteudo, String nomeArquivo) {
		ByteArrayInputStream input = new ByteArrayInputStream(conteudo.getBytes());
		return client.putObject(bucketName, nomeArquivo, input, new ObjectMetadata());
	}

	/**
	 * Uploads a new object to the specified Amazon S3 bucket. This method will
	 * create a <code>PutObjectRequest</code>, containing all the details of the
	 * request, including the bucket to upload to, the key the object will be
	 * uploaded under, and the file or input stream containing the data to upload.
	 * 
	 * @param bucketName
	 *            The name of an existing bucket to which the new object will be
	 *            uploaded.
	 * @param fileKey
	 *            The key under which to store the new object.
	 * @param file
	 *            The path of the file to upload to Amazon S3.
	 * @return A {@link PutObjectResult} object containing the information returned
	 *         by Amazon S3 for the newly created object.
	 * @throws SdkClientException
	 *             If any errors are encountered in the client while making the
	 *             request or handling the response.
	 * @throws AmazonServiceException
	 *             If any errors occurred in Amazon S3 while processing the request.
	 * @see AmazonS3#putObject(PutObjectRequest)
	 */
	public PutObjectResult enviaFileParaBucket(String bucketName, String fileKey, File file) {
		return client.putObject(new PutObjectRequest(bucketName, fileKey, file));
	}

	/**
	 * Uploads a new object to the specified Amazon S3 bucket. This method will
	 * create a <code>PutObjectRequest</code>, containing all the details of the
	 * request, including the bucket to upload to, the key the object will be
	 * uploaded under, and input stream containing the data to upload.
	 * 
	 * @param bucketName
	 *            The name of an existing bucket to which the new object will be
	 *            uploaded.
	 * @param fileKey
	 *            The key under which to store the new object.
	 * @param inputStream
	 *            The data to upload to amazonS3.
	 * @param metadata
	 *            The metadata for the file being uploaded
	 * @param cannedAcl
	 *            The access level the file will have on S3
	 * @return A {@link PutObjectResult} object containing the information returned
	 *         by Amazon S3 for the newly created object.
	 * @throws SdkClientException
	 *             If any errors are encountered in the client while making the
	 *             request or handling the response.
	 * @throws AmazonServiceException
	 *             If any errors occurred in Amazon S3 while processing the request.
	 * @see AmazonS3#putObject(PutObjectRequest)
	 */
	public PutObjectResult enviaFileParaBucket(String bucketName, String fileKey, InputStream inputStream,
			ObjectMetadata metadata, CannedAccessControlList cannedAcl) {
		PutObjectRequest por = new PutObjectRequest(bucketName, fileKey, inputStream, metadata);
		por.setCannedAcl(cannedAcl);
		return client.putObject(por);
	}

	/**
	 * Gets the object stored in Amazon S3 under the specified bucket and key.
	 * Returns null if the specified constraints weren't met. Be extremely careful
	 * when using this method; the returned Amazon S3 object contains a direct
	 * stream of data from the HTTP connection. The underlying HTTP connection
	 * cannot be reused until the user finishes reading the data and closes the
	 * stream. Also note that if not all data is read from the stream then the SDK
	 * will abort the underlying connection, this may have a negative impact on
	 * performance.
	 * 
	 * @param bucketName
	 * @param key
	 * @return S3Object
	 * @throws IOException
	 */
	public S3Object obtemDoBucket(String bucketName, String key) throws IOException {
		GetObjectRequest request = new GetObjectRequest(bucketName, key);
		return client.getObject(request);
	}

	/**
	 * Remove a object to the specified bucket,
	 * 
	 * @param bucketName
	 * @param fileName
	 */
	public void removeDoBucket(String bucketName, String fileName) {
		client.deleteObject(bucketName, fileName);
	}

	/**
	 * Verify if a given bucket exists
	 * 
	 * @param bucket
	 * @return
	 */
	public boolean existeBucket(String bucketName) {
		return client.doesBucketExistV2(bucketName);
	}

	public String generateKeyForfile() {
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat(DateFormatter.formatShortUTC(currentDate));
		return format.format(currentDate) + UUID.randomUUID().toString();
	}

}
