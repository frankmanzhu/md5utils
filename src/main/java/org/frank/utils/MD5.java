package org.frank.utils;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.security.MessageDigest;
import picocli.jansi.graalvm.AnsiConsole;

@Command(name = "MD5", mixinStandardHelpOptions = true,
        showAtFileInUsageHelp = true,
        header = "MD5 Utility.",
        headerHeading = "@|bold,underline Usage|@:%n%n",
        synopsisHeading = "%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        version = "my cmd app version 1.0",
        description = "Prints the checksum (MD5 by default) of a file to STDOUT.",
        footer = "Frank Org Copyright(c) 2020"
)
public class MD5 implements Runnable {

    @Option(names = { "-v", "--verbose" },
            description = "Verbose mode. Helpful for troubleshooting.")
    private boolean verbose;

    @Parameters(description = "The files whose checksum to calculate.")
    private File[] files;

    @Option(names = {"-a", "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...")
    private String algorithm = "MD5";

    public void run() {

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            if (files == null) {
                System.out.println("Add MD5 itself to the checksum");
                files = new File[1];
                files[0] = new File("./MD5");
            }

            for (File file : files) {
                md.update(Files.readAllBytes(file.toPath()));
                byte[] digest = md.digest();
                String fileChecksum = bytesToHex(digest).toUpperCase();
                System.out.println("File name is : " + file.getName() + " and its md5 is: " + fileChecksum);
            }
        } catch (Exception e) {
            if (verbose) {
                e.printStackTrace();
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
//        ColorScheme colorScheme = new ColorScheme.Builder()
//                .commands    (Style.bold, Style.underline)    // combine multiple styles
//                .options     (Style.fg_yellow)                // yellow foreground color
//                .parameters  (Style.fg_yellow)
//                .optionParams(Style.italic)
//                .errors      (Style.fg_red, Style.bold)
//                .stackTraces (Style.italic)
//                .build();
//                .setColorScheme(colorScheme)

        try (AnsiConsole ignored = AnsiConsole.windowsInstall()) {
            int exitCode = new CommandLine(new MD5()).execute(args);
            System.exit(exitCode);
        }
    }
}
