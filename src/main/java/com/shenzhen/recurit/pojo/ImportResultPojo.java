package com.shenzhen.recurit.pojo;

import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class ImportResultPojo<T> {

      private List<T> listT;
      private Map<Integer,ExportsPojo> exportMap;
      private File templateFile;
}
