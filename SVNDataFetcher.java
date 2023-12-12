package helloworld;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;

import java.io.File;
import java.util.ArrayList;
public class SVNDataFetcher implements DataFetcher {
    private final SvnOperationFactory svnOperationFactory;

    public SVNDataFetcher() {
        svnOperationFactory = new SvnOperationFactory();
    }

    @Override
    public ArrayList<File> downloadPackage(String sourceUrl, boolean isLambdaEnvironment) {
        ArrayList<File> downloadedFiles = new ArrayList<>();

        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(sourceUrl);

            // Handle Lambda environment specific path
            String checkoutPath = isLambdaEnvironment ? "/tmp" : "temp";
            File checkoutDirectory = new File(checkoutPath);

            // Setup SVN client manager
            SVNClientManager clientManager = SVNClientManager.newInstance();
            SVNUpdateClient updateClient = clientManager.getUpdateClient();
            updateClient.setIgnoreExternals(false);

            // Perform checkout
            updateClient.doCheckout(svnurl, checkoutDirectory, SVNRevision.HEAD, SVNRevision.HEAD, true);

            // Populate the ArrayList with checked-out files
            populateDownloadedFilesList(checkoutDirectory, downloadedFiles);
        } catch (SVNException e) {
            throw new RuntimeException(e);
        } finally {
            svnOperationFactory.dispose();
        }

        return downloadedFiles;
    }

    private void populateDownloadedFilesList(File directory, ArrayList<File> downloadedFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    populateDownloadedFilesList(file, downloadedFiles);
                } else {
                    downloadedFiles.add(file);
                }
            }
        }
    }
}
