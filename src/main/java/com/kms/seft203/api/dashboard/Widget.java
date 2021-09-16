package com.kms.seft203.api.dashboard;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.kms.seft203.domain.dashboard.Dashboard;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "widgets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Widget {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String title;
    private String widgetType;
    private Integer minWidth;
    private Integer minHeight;

    @ElementCollection(targetClass=String.class)
    @MapKeyColumn(name="config")
    private Map<String, Object> configs;

    @ManyToOne
    @JoinColumn(name = "dashboard")
    private Dashboard dashboard;


}
