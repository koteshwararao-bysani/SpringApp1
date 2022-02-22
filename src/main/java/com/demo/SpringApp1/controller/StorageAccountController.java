package com.demo.SpringApp1.controller;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.EnumSet;

@RestController
@RequestMapping("/storageaccountdemo")
public class StorageAccountController {

    //@Value("azure-blob://containerpublic/candlestick-PATTERN.jpg")
    //@Value("https://koteshstorageaccount.blob.core.windows.net/containerpublic/candlestick-PATTERN.jpg")
    @Value("https://koteshstorageaccount.blob.core.windows.net/containerpublic/candlestick-PATTERN.jpg?sp=r&st=2022-02-22T14:57:21Z&se=2022-02-22T22:57:21Z&spr=https&sv=2020-08-04&sr=b&sig=2mEPTzti1NnwOQfVMCtcReZmr36lMD4QGgJFBqvci0Y%3D")
    private Resource publicBlobFile;

    @Value("https://privatestorageaccount7.blob.core.windows.net/imagecontainer/Forex-Chart-Patterns-Cheatsheet.png")
    private Resource privateBlobFile;


    @GetMapping("/readBlobFile")
    public String readBlobFile() throws IOException {
        System.out.println("i am from StorageAccountController :: readBlobFile()");
        return StreamUtils.copyToString(
                this.publicBlobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @GetMapping("/download/publicfile")
    public ResponseEntity<ByteArrayResource> readPublicBlobFile() throws IOException {
        System.out.println("i am from StorageAccountController :: readPublicBlobFile()");
        byte[] data = this.publicBlobFile.getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("content-type","application/octet-stream")
                .header("content-disposition","attachment; filename= file-image")
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/download/privatefile")
    public ResponseEntity<ByteArrayResource> readBlobPrivateFile() throws IOException {
        System.out.println("i am from StorageAccountController :: readBlobPrivateFile()");
        byte[] data = this.privateBlobFile.getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("content-type","application/octet-stream")
                .header("content-disposition","attachment; filename= file-image")
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/download/imagefile")
    public ResponseEntity<ByteArrayResource> getImageFile() throws IOException, URISyntaxException, InvalidKeyException, StorageException {
        System.out.println("i am from StorageAccountController :: getImageFile()");

        CloudBlobContainer cloudBlobContainerObj = this.getCloudBlobContainer();
        String sasContainerToken = this.genContainerSASToken(cloudBlobContainerObj);
        ListBlobItem blobList  = cloudBlobContainerObj.listBlobs().iterator().next();
        //ListBlobItem blobList = (ListBlobItem) getCloudBlobContainer().listBlobs();
        //Resource blobResource = new UrlResource(blobList.getUri());
        Resource blobResource = new UrlResource(blobList.getUri()+"?"+sasContainerToken);
        byte[] data = blobResource.getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("content-type","application/octet-stream")
                .header("content-disposition","attachment; filename= image-file")
                .body(new ByteArrayResource(data));
    }


    String connectionString = "DefaultEndpointsProtocol=https;AccountName=privatestorageaccount7;AccountKey=stogjQH7yZfz1KHrdeUOwrTVCTEejgx8NEPXaql1vD7iWUG+a4vasDVePq83Nmv2U6Cm8p4JmhqV+ASt8mBz2Q==;EndpointSuffix=core.windows.net";
    //String connectionString = "BlobEndpoint=https://privatestorageaccount7.blob.core.windows.net/;QueueEndpoint=https://privatestorageaccount7.queue.core.windows.net/;FileEndpoint=https://privatestorageaccount7.file.core.windows.net/;TableEndpoint=https://privatestorageaccount7.table.core.windows.net/;SharedAccessSignature=sv=2020-08-04&ss=bfqt&srt=c&sp=rwdlacupitfx&se=2022-02-22T23:37:48Z&st=2022-02-22T15:37:48Z&spr=https&sig=JuNVBA9BBh5oEwxCSmxFiXmaRleIqzIajn14RMhOzdQ%3D";
    String containerName = "imagecontainer";

    public CloudBlobContainer getCloudBlobContainer() throws URISyntaxException, InvalidKeyException, StorageException {
        CloudBlobContainer cloudBlobContainer = null;
        CloudStorageAccount cloudStorageAccount;
        cloudStorageAccount = CloudStorageAccount.parse(connectionString);
        CloudBlobClient cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
        cloudBlobContainer = cloudBlobClient.getContainerReference(containerName);
        return cloudBlobContainer;
    }

    public String genContainerSASToken(CloudBlobContainer cloudBlobContainer) throws InvalidKeyException, StorageException {
        SharedAccessBlobPolicy sasConstraints = new SharedAccessBlobPolicy();
        Date expirationDate = Date.from(Instant.now().plus(Duration.ofDays(1)));
        sasConstraints.setSharedAccessExpiryTime(expirationDate);
        EnumSet<SharedAccessBlobPermissions> permissions = EnumSet.of(
                SharedAccessBlobPermissions.WRITE,
                SharedAccessBlobPermissions.LIST,
                SharedAccessBlobPermissions.READ,
                SharedAccessBlobPermissions.DELETE);
        sasConstraints.setPermissions(permissions);

        //Generate the shared access signature on the container, setting the constraints directly on the signature.
        String sasContainerToken = cloudBlobContainer.generateSharedAccessSignature(sasConstraints, null);
        System.out.println("sasContainerToken :: "+sasContainerToken);

      return  sasContainerToken;
    }

}
