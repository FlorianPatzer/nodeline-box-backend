package de.nodeline.box.application.secondaryadapter.nifi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* EXAMPLE POST
 * {"revision":
 *  {"clientId":"9dd43a8f-0c27-4160-9c2a-d9f291d8ec74",
 *      "version":0
 *  },
 *  "disconnectedNodeAcknowledged":false,
 *  "component":
 *      {"position":
 *          {"x":-506,
 *              "y":-333.3000030517578
 *          },
 *          "name":"testProcessGroup"
 *      }
 * }

 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessGroupDTO {
    private String id;
    private String versionedComponentId;
    private String parentGroupId;
    private PositionDTO position;
    private String name;
    private String comments;    
}

