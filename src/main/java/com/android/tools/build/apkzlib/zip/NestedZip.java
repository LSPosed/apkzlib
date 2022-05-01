package com.android.tools.build.apkzlib.zip;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

public class NestedZip extends ZFile {
    final ZFile target;
    final StoredEntry entry;

    public interface NameCallback {
        String getName(ZFile file) throws IOException;
    }

    public NestedZip(NameCallback name, ZFile target, File src, boolean mayCompress) throws IOException {
        super(src, new ZFileOptions(), true);
        this.target = target;
        this.entry = target.add(name.getName(this), directOpen(0, directSize()), mayCompress);
    }

    /**
     * @return true if lfh is consistent with cdh otherwise inconsistent
     */
    public boolean addFileLink(@Nonnull StoredEntry srcEntry, @Nonnull String dstName) throws IOException {
        var srcName = srcEntry.getCentralDirectoryHeader().getName();
        var offset = srcEntry.getCentralDirectoryHeader().getOffset() + srcEntry.getLocalHeaderSize();
        if (srcName.equals(dstName)) {
            target.addNestedLink(entry, dstName, srcEntry, srcEntry.getCentralDirectoryHeader().getOffset(), true);
            return true;
        } else if (offset < MAX_LOCAL_EXTRA_FIELD_CONTENTS_SIZE) {
            target.addNestedLink(entry, dstName, srcEntry, offset, false);
            return true;
        }
        return false;
    }

    public boolean addFileLink(@Nonnull String srcName, @Nonnull String dstName) throws IOException {
        var srcEntry = get(srcName);
        if (srcEntry == null)
            throw new IOException("Entry does not exist in nested zip");
        return addFileLink(srcEntry, dstName);
    }

    public ZFile getTarget() {
        return target;
    }

    public StoredEntry getEntry() {
        return entry;
    }
}
