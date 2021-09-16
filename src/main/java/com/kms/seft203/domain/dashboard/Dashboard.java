package com.kms.seft203.domain.dashboard;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.kms.seft203.api.dashboard.Widget;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dashboards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String userId;
    private String title;
    private String layoutType;

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "dashboard_widget", 
				joinColumns = @JoinColumn(name = "dashboard_id"), 
				inverseJoinColumns = @JoinColumn(name = "widget_id"))
    private List<Widget> widgets;
}
